# AssetRegistration: async indexing via a flattened AssetRegistrationIndex document

## Why

`AssetRegistration` is the most heavily-related entity in the fixed-asset module - it carries
`@ManyToMany`/`@ManyToOne` relations to `AssetCategory`, `Dealer`, `AssetAccessory`,
`PaymentInvoice`, `PurchaseOrder`, `DeliveryNote`, `JobSheet`, `BusinessDocument`,
`AssetWarranty`, `ServiceOutlet`, `Settlement`, and more. `InternalAssetRegistrationServiceImpl`
(the implementation actually wired behind `AssetRegistrationResourceProd`, per this project's
Extension/Prod convention) used to write straight to `AssetRegistrationSearchRepository` -
Elasticsearch's own copy of the full `AssetRegistration` entity graph - synchronously, inside the
same `@Transactional` method that persisted the row to Postgres:

```java
assetRegistration = assetRegistrationRepository.save(assetRegistration);
AssetRegistrationDTO result = assetRegistrationMapper.toDto(assetRegistration);
assetRegistrationSearchRepository.save(assetRegistration); // <- could fail the whole request
```

Two concrete problems followed directly from that:

1. **A failing or slow Elasticsearch write could fail (and roll back) an otherwise-successful
   database save.** Reported symptom: setting up an `AssetRegistration`'s full web of
   relationships (accessories, invoices, etc.) from erp-client would fail outright, because the
   resulting document - with everything eagerly loaded and serialized - is large, deeply nested,
   and, per this session's own findings elsewhere, sensitive to Elasticsearch index state (a
   missing monthly index alone is enough to fail the whole save).
2. Searching that same index meant results were only ever as fresh as the last successful write,
   with no way to tell a genuinely-deleted `AssetRegistration` apart from an ES document that
   simply never got cleaned up.

## What changed

**New document, used only for matching, never returned directly:** `AssetRegistrationIndex`
(`domain/AssetRegistrationIndex.java`) is a flat, ES-only projection - scalar fields plus lists of
related entities' identifying names/numbers (e.g. `accessoryDescriptions`,
`paymentInvoiceNumbers`, `businessDocumentTitles`), never the related entities themselves. It also
uses a **fixed index name** (`assetregistrationindex`, no month suffix) rather than
`AssetRegistration`'s own `assetregistration-yyyy-MM` pattern - deliberately, so there is no
monthly index to go missing (see the unrelated `index_not_found_exception` incident this session
also hit and worked around for `AssetRegistration`'s own index).

**Save/update/delete now queue the index write instead of doing it inline:**
`InternalAssetRegistrationServiceImpl.save/partialUpdate/delete` publish a message via the new
`AssetRegistrationIndexProducer` (Kafka topic `asset-registration-index`, following the exact
config/producer/consumer pattern already established by `LeasePaymentReindexProducer`/
`LeasePaymentReindexConsumer` for lease payments) instead of calling
`assetRegistrationSearchRepository` directly. The database transaction commits (or rolls back)
purely on its own merits now - Elasticsearch's availability can no longer affect it.
`AssetRegistrationIndexConsumer` does the actual write: it always re-fetches the entity fresh
from Postgres by id (via `findOneWithEagerRelationships`) rather than trusting anything in the
message, maps it through `AssetRegistrationIndexMapper`, and saves/deletes the
`AssetRegistrationIndex` document.

**Search now matches, then hydrates from Postgres:** `InternalAssetRegistrationServiceImpl.search`
queries `AssetRegistrationIndexSearchRepository` for matching ids, then re-fetches those ids'
real `AssetRegistration` rows via `assetRegistrationRepository.findAllById(...)` before mapping to
DTOs and returning. A stale or orphaned `AssetRegistrationIndex` document (one whose underlying
row is gone, or one written by a message that got redelivered after its real save was rolled
back) simply won't produce a result - `findAllById` can't return a row that isn't there. Total
count still comes from the Elasticsearch page (for pagination), but every result on the page is
one erp-system's own database vouches for.

**Startup / full reindex:** `AssetRegistryIndexingService` (the existing
`AbstractStartUpBatchedIndexService` hook, unchanged in how it's registered/triggered - still
responds to `GET /api/index/run-index` and to `ERP_INDEX_REBUILD_ENABLED` on boot) now builds
`AssetRegistrationIndex` documents via the same `AssetRegistrationIndexMapper` used by the queue
consumer, and writes them to `AssetRegistrationIndexSearchRepository`. A full reindex and an
incremental per-save update produce identical documents by construction, since both go through
the one mapper.

## Caught during live verification: send-before-commit race

Rebuilt the image (`./mvnw -Pprod -DskipTests jib:dockerBuild`, tag `ghacupha/erp-system:1.8.3`,
same tag both the prod-like and dev compose stacks reference) and recreated the running
`erp-system-server` container with `ERP_INDEX_ENABLED`/`ERP_INDEX_REBUILD_ENABLED=TRUE`, so
startup did a full reindex against the real Kafka/Elasticsearch/Postgres in this environment - not
a mock. All 6648 `AssetRegistration` rows indexed cleanly with zero errors.

Then reproduced the originally-reported failure mode directly: created an `AssetRegistration` via
`POST`, then `PATCH`ed in two `assetAccessories` and two `paymentInvoices` (the exact "map the
asset to accessories and invoices" step that used to fail). It returned `200`, not `500` -
confirming the core fix. But checking the resulting `AssetRegistrationIndex` document immediately
after showed `accessoryDescriptions: []` and `paymentInvoiceNumbers: []` - empty, despite the
relations having just been attached.

Root cause: `assetRegistrationIndexProducer.sendIndexMessage(...)` was called *inside* the still-
open `@Transactional` method, before Postgres had committed the new join-table rows. Kafka's send
isn't transaction-aware by default, so `AssetRegistrationIndexConsumer` could - and did - run its
own fresh `findOneWithEagerRelationships` fetch before the accessory/invoice associations were
visible outside the original transaction, indexing a stale, accessory-less snapshot moments after
the attach.

Fixed by deferring the send: `InternalAssetRegistrationServiceImpl` now registers a
`TransactionSynchronization` (`afterCommit()`) instead of calling the producer directly, falling
back to an immediate send only if no transaction is active. Rebuilt, redeployed, and re-ran the
identical create-then-attach sequence: `accessoryDescriptions` and `paymentInvoiceNumbers` were
populated correctly this time. Delete was verified the same way (create → delete → confirm the
`AssetRegistrationIndex` document is gone).

## Follow-up: the same treatment applied to AssetAccessory

`AssetAccessoryServiceImpl` (generated) had the identical shape - synchronous
`assetAccessorySearchRepository.save(...)` inside the save transaction, against an entity with
its own wide web of relations (warranties, invoices, service outlets, settlements, POs, delivery
notes, job sheets, business documents). Since `AssetAccessoryResourceProd` (unlike
`AssetRegistrationResourceProd`) had no `Internal*` extension seam yet, one was added:
`internal/service/assets/InternalAssetAccessoryService(Impl)`, mirroring
`InternalAssetRegistrationService(Impl)` exactly, including the after-commit fix from the start
(no separate race to catch this time - it was already known). `AssetAccessoryResourceProd` now
depends on this new interface instead of the generated `AssetAccessoryService`. New
`AssetAccessoryIndex` (fixed index name `assetaccessoryindex`), `AssetAccessoryIndexMapper`,
Kafka producer/consumer (topic `asset-accessory-index`), and `AssetAccessoryIndexingService`
(startup/reindex) all mirror their AssetRegistration counterparts.

Verified live the same way: rebuilt, redeployed, confirmed 32 accessories reindexed cleanly on
startup, created an accessory, `PATCH`ed in two `paymentInvoices`, confirmed `200` and the index
document showed `paymentInvoiceNumbers: ["2610", "839"]` correctly (no stale-read repeat needed -
the after-commit synchronization was already in place), then deleted it and confirmed the index
document was removed too.

## Not covered in this pass

- **`AssetRegistrationAndDepreciationServiceImpl`** (`internal/service/assets/`) duplicates the
  old synchronous save/search logic this change replaced, but isn't wired to any REST resource
  (no `@RestController` references `AssetRegistrationAndDepreciationService`) - confirmed dead
  code, not touched. If it's ever wired up, it needs the identical fix.
- **Test verification:** `AssetRegistrationResourceIT` (the Prod-resource test) was updated to
  stop asserting the now-removed synchronous `AssetRegistrationSearchRepository` calls and to
  mock `AssetRegistrationIndexSearchRepository` instead for its search test, with `@EmbeddedKafka`
  added so the producer's `send()` has a broker to talk to. That embedded broker fails to start in
  this session's environment (JDK 19 / Windows) with `kafka.common.KafkaException` /
  `TimeoutException` during topic creation - reproduced identically on the pre-existing,
  untouched `LeasePaymentUploadServiceIT`, which uses the same `@EmbeddedKafka` mechanism. This is
  a pre-existing environment limitation, not something this change caused or could fix from
  application code. The unaffected baseline (`web.rest.AssetRegistrationResourceIT`, which still
  goes through the untouched, generated `AssetRegistrationServiceImpl`) passed all 100 of its
  tests, confirming this change didn't regress anything outside its own scope. Real end-to-end
  verification instead happened against the actual running dev stack's real Kafka broker (see
  the restart/verify note this doc's companion user story links to).
