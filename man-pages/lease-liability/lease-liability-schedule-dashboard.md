# Lease Liability Schedule Dashboard

## Overview
A dedicated dashboard now surfaces the IFRS16 liability schedule for a single contract, combining high-level metrics with the detailed monthly rows that underpin them. Lease managers can reach the screen from the client route `#/lease-liability-schedule-view/{contractId}`. Access is restricted to **ROLE_LEASE_MANAGER**.

## Implementation summary
- The Angular feature module (`lease-liability-schedule-view`) coordinates four services to assemble the view: the lease contract, the linked liability, repayment periods, and the ordered schedule items.
- All lookups are performed with `leaseContractId.equals={contractId}` filters and explicit `sort=sequenceNumber,asc` on the schedule collection to preserve amortisation order.
- Period information is normalised to dayjs instances so that existing `formatMediumDate` pipes can be reused for display.
- The default reporting window is the most recent repayment period, but the dropdown allows users to revisit any earlier period.

## Reporting behaviour
- Headline cards display the original liability amount, the liability start, the selected period end date, and total cash/principal/interest/outstanding/interest-payable amounts calculated via array reductions across the filtered schedule rows.
- The table presents each month’s balances (opening, cash, principal, interest, outstanding, interest payable) and annotates “days since previous payment” by comparing consecutive period boundaries.
- Selecting a new period recomputes the filtered list and totals; the empty state banner appears if no rows match the selection.
- Loading feedback and inline error alerts guard the combined forkJoin request so incomplete responses do not render partial dashboards.

## Usage notes
- The feature assumes that the backend links schedule items to repayment periods; if a contract lacks periods the selector is disabled and the dashboard exposes only the initial liability context.
- All numeric outputs use the shared number pipes (`number: '1.2-2'`), guaranteeing consistency with other financial dashboards.
