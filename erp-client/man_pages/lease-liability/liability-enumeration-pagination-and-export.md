# Liability enumeration pagination and exports

## Overview
Recent updates tighten visibility into generated liability enumerations and their present values. Each batch now records the local request date-time, list responses default to the newest entries first, and UI tables surface booking information with paging to avoid unwieldy scrolls. Present value pages identify the contract they belong to and expose CSV/XLSX exports for offline review.

## Back-end adjustments
- **New timestamp**: `LiabilityEnumeration.requestDateTime` captures the local date-time when an enumeration is queued. It is persisted in the database and indexed for search and sorting.
- **Sorted listing**: `/api/leases/liability-enumerations` now defaults to `requestDateTime` in descending order, returning pageable results.
- **Present value defaults**: `/api/leases/present-value-enumerations` defaults to ascending `sequenceNumber` ordering and a higher page size to minimize client round trips.

## Front-end behaviour
- The liability enumeration list shows the lease booking ID, requested-at timestamp, and uses infinite scroll pagination while keeping the newest items first.
- The present value enumeration page titles itself with the lease contract identifier and includes export buttons for CSV and XLSX snapshots of the visible schedule.
- Export data respects the server-side ordering so downloaded files match the on-screen sequence.
