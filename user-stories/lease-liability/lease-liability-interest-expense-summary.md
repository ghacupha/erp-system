# Story: Generate the lease liability interest expense summary

## Persona
*Finance accountant responsible for IFRS 16 postings*

## Narrative
- **Given** I am preparing the monthly IFRS 16 journals
- **When** I open the "Lease Liability Interest Expense Summary" report and supply the lease period for the month I am posting
- **Then** the system returns a list of leases showing the debit and credit accounts, the current month interest expense, the
  cumulative interest for the year to date and the cumulative interest up to the previous month
- **And** the narration column combines the booking ID and lease title so that I can paste the description directly into the
  posting template
- **And** the lease number column mirrors the identifiers in the core banking system to simplify reconciliation.

## Acceptance criteria
1. The report parameter accepts a single lease period identifier and returns only data for leases that have schedule items in
   that period.
2. Current period interest equals the sum of `interestAccrued` for the specified lease period.
3. Cumulative annual interest sums January-to-current `interestAccrued` within the same fiscal year as the supplied period.
4. Cumulative last month equals the cumulative annual value less the current period amount, therefore zero for January.
5. Debit and credit account numbers originate from the TA Lease Interest Accrual Rule linked to the lease contract.
