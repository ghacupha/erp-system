# Lease Liability Dashboard – User Story

## Persona
**Lease Manager** – reviews IFRS16 balances and monthly amortisation activity for a specific lease contract.

## Goal
Quickly confirm the outstanding liability and payment mix for a reporting period without exporting raw schedule data.

## Pre-conditions
- The contract has an IFRS16 lease record, liability, repayment periods, and compiled schedule items.
- The user holds the `ROLE_LEASE_MANAGER` authority.

## Story
**As a** Lease Manager  
**I want** to open the lease liability schedule dashboard for a contract  
**So that** I can see the latest balances and drill into the period-by-period movements.

### Acceptance criteria
1. Navigating to `#/erp/lease-liability-schedule-view/{contractId}` loads the dashboard showing the contract title, the liability reference, and the latest reporting period selected by default.
2. Headline cards summarise the initial liability, schedule start, reporting close date, and the cash/principal/interest/outstanding/interest-payable totals for the active period.
3. The monthly table lists each schedule row for the chosen period, including payment days since the previous entry and formatted amounts.
4. Changing the reporting-period dropdown re-filters the table and recalculates the totals without reloading the page.
5. If no schedule rows exist for the chosen period the table displays an empty-state message while leaving the header metrics unchanged.
