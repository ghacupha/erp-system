# Liability enumeration booking selection store

## Summary

The liability enumeration list now dispatches an NgRx action when opening the present value view. The action captures both the enumeration identifier and the lease contract bookingId so the selection survives navigation and API latency.

## Design notes

- Introduced a dedicated `liabilityEnumeration` feature slice under the client `app/erp/erp-leases/liability-enumeration/state` path and registered it with `StoreModule.forFeature` in the feature module.
- The list component dispatches `setSelectedLiabilityEnumeration` with the bookingId (or lease contract id fallback) before routing to present values.
- The present value component subscribes to the selector for the stored bookingId and prefers it for the header/title, while still falling back to API payloads.
- Subscriptions in the present value component use `takeUntil` to avoid leaks during navigation.
