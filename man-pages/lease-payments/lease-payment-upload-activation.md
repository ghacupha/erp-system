# Lease payment upload activation and pagination (client + server)

## Context
Lease payment uploads were previously listed as a flat list with only a deactivate action. Operations teams need to review recent uploads in chronological order, reactivate batches that were deactivated in error, and trigger search reindexing so downstream search views stay in sync with activation changes.

## Server workflow
- **Paged endpoint**: `/api/leases/lease-payment-uploads` now accepts pageable parameters with a default page size of 5 and sorts by `createdAt` descending. The endpoint returns JHipster pagination headers (`X-Total-Count`, `Link`).
- **Activation endpoint**: `/api/leases/lease-payment-uploads/{id}/activate` mirrors the deactivate flow. It:
  - Marks the `LeasePaymentUpload` as active with status `ACTIVE`.
  - Sets all associated `LeasePayment` rows back to `active = true`.
  - Dispatches a reindex message via `LeasePaymentReindexProducer`, passing the affected IDs and the active flag so the search index reflects the new state.
- **Deactivation flow**: unchanged in intent, but now shares the reindex helper that accepts the active flag, ensuring both activation and deactivation go through the Kafka-based reindex job.

## Client workflow
- The "Previous uploads" table is now page-aware with a fixed size of 5. Pagination controls and item counts use the backend headers to navigate through uploads in reverse chronological order.
- Each row shows either a **Deactivate** or **Activate** button in the same action slot. The activate action calls the new backend endpoint and updates the local list item in place.

## Rationale
These changes let operators page through recent uploads, confirm whether a batch is active, and toggle activation while keeping the lease payment search index synchronized. Sorting newest-first emphasizes the most recent activity without manual filtering.
