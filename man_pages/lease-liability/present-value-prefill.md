# Lease liability form now prefills from present value enumerations

## Context
Lease managers capture liabilities after uploading lease payments and running a liability enumeration. The enumeration produces `PresentValueEnumeration` rows and updates the linked `LiabilityEnumeration` with the rate and request timestamp. Previously, the lease liability form required manual entry of the liability amount and rate even when the present value data already existed.

## Change
`InternalLeaseLiabilityService` now looks up the most recent `LiabilityEnumeration` for the selected IFRS16 lease contract when saving or updating a lease liability. It gathers the related `PresentValueEnumeration` lines, sums their present values to two decimals, and uses that total as the liability amount. The interest rate on the enumeration is also applied to the liability. Prefilling is skipped when no enumeration exists or when no present value rows are stored.

## Rationale
Using the persisted present value schedule ensures the liability matches the latest discounted cashflows generated from the CSV upload pipeline. This reduces manual data entry, keeps the liability in sync with the amortization calculation, and provides a single source of truth for the rate applied during enumeration.

## Impacted components
- Backend: `InternalLeaseLiabilityService` pulls data from `LiabilityEnumerationRepository` and `PresentValueEnumerationRepository`.
- Data: relies on `LiabilityEnumeration.requestDateTime` ordering to pick the newest run for the contract.
- UI: the lease liability form continues to preload data; the liability amount and rate now reflect the latest present value enumeration without user re-entry.
