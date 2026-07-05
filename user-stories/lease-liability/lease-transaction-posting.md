# Lease transaction posting user stories

## Story 1: Lease accountant posts schedule-based lease transactions

**Persona:** Lease accountant responsible for monthly posting.

**Steps:**
1. Navigate to the lease liability schedule batch area.
2. Select the schedule batch to post.
3. Trigger the posting action for repayments, interest accruals, and interest paid transfers.

**Expected outcome:**
- The system posts transaction details in bulk using the configured lease posting rules.
- Each posted line references the fiscal month end date and includes the expected description and amount.

## Story 2: Lease accountant posts recognition and amortization transactions

**Persona:** Lease accountant preparing recognition and amortization entries.

**Steps:**
1. Navigate to the lease recognition or ROU amortization batch action.
2. Start the posting task.

**Expected outcome:**
- The system posts recognition and amortization entries using the configured debit/credit accounts.
- Each entry is linked to the requisition identifier for audit tracking.
