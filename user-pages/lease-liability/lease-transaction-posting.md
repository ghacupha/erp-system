# Lease transaction posting

## Overview

Lease transaction posting turns lease schedules and recognition data into accounting entries. The system uses configured lease posting rules to resolve the debit and credit accounts and posts all eligible entries in bulk during the batch process.

## How to post schedule-based transactions

1. Open the lease liability schedule batch view.
2. Choose the schedule batch you want to post.
3. Run the batch posting action for lease repayment, interest accrual, and interest paid transfer.
4. Confirm that the batch completes successfully.

**Result:** The system creates transaction details for every non-zero schedule line, using the fiscal month end date and the configured lease posting rules.

## How to post recognition and amortization entries

1. Navigate to the lease recognition or ROU amortization posting action.
2. Trigger the batch job.
3. Review the posted entries for the requisition identifier.

**Result:** Recognition and amortization postings appear as transaction details tied to the requisition, with the debit/credit accounts resolved from the configured rules.
