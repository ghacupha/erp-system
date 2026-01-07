# TA ROU Recognition Rule Auto-Population

## Overview
The TA Recognition ROU Rule update form now reacts to lease contract selection changes by
fetching the full lease contract record and auto-populating the debit and credit accounts
from the lease template when available. This reduces manual data entry and keeps the rule
aligned with the template-defined ROU recognition accounts.

## Workflow
1. The update component registers a `leaseContract` value-change subscription during
   initialization.
2. When a lease contract is selected (and has an ID), the component loads the full lease
   contract via the lease contract service.
3. If the response includes a lease template with ROU recognition debit/credit accounts,
   the form patches the `debit` and `credit` fields and ensures those accounts are
   available in the transaction account collection.
4. If the lease contract has no template or the template omits either account, the
   form leaves existing values intact.

## Rationale
Lease templates already encode the correct ROU recognition accounts. Surfacing those
accounts automatically in the rule edit form ensures consistency across lease setup and
reduces the risk of incorrect manual selections.

## Key Components
- Update component subscription: `TARecognitionROURuleUpdateComponent`.
- Lease contract fetch: `IFRS16LeaseContractService.find`.
- Account patching: `rouRecognitionDebitAccount` and
  `rouRecognitionCreditAccount` from `leaseTemplate`.

## Notes
This change is UI-focused and does not alter persistence logic. The existing save and
copy flows remain unchanged.
