# ROU Recognition Posting Rule Guidance

## Purpose
ROU recognition postings now use the posting rule engine. Use the lease template
accounts to choose the debit and credit accounts when creating the posting rule templates.
This keeps the ROU recognition postings aligned with the lease setup.

## How to Use
1. Open the posting rule administration page and create a rule for module `LEASE` with event `LEASE_ROU_RECOGNITION`.
2. Choose a lease contract or reference the lease template to determine the correct accounts.
3. Add a posting template and select the debit and credit accounts:
   - If the lease template defines ROU recognition accounts, use those as the default selections.
   - If the template does not include these accounts, select the appropriate ledgers manually.

## Tips
- Ensure the lease template linked to the contract includes ROU recognition debit and
  credit accounts so the posting rule templates match the intended mappings.
