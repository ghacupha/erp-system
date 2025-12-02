# Lease payment upload activation (server)

## Scope
Covers the ERP server changes enabling activation toggles for lease payment uploads and paginated retrieval of recent uploads.

## Changes
- Added pageable support to `/api/leases/lease-payment-uploads` (default size 5, sorted by `createdAt` DESC) with pagination headers so the client can page through newest uploads first.
- Introduced `/api/leases/lease-payment-uploads/{id}/activate`, which:
  - Sets the upload `active` flag to true and labels the status `ACTIVE`.
  - Marks all linked `LeasePayment` entities as active and persists them.
  - Reuses the Kafka-based `LeasePaymentReindexProducer` to broadcast the updated activation state for search indexing.
- Refactored the reindex helper to accept an `active` parameter so both activation and deactivation enqueue the correct search-index state.

## Operational notes
The activation endpoint mirrors the existing deactivation semantics, throwing an error when an upload ID cannot be found. Reindex messages are dispatched after transaction commit to avoid indexing partially-updated data.
