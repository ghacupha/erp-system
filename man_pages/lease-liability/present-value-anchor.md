# Present value anchor date update

## Context
Lease liability present value calculations now use a fixed anchor date of **2019-01-01**. This ensures discounting and sequencing start from the same regulatory cutoff used in reporting. Payments dated before the anchor are excluded from the computation.

## Behaviour
- The calculator filters out lease payments before 2019-01-01.
- If no payments remain after filtering, it raises a clear `IllegalArgumentException`.
- Sequencing starts at January 2019 and inserts zero-amount periods for months until the first eligible payment, preserving the discounting timeline.

## Impact
Data files containing historic lease payments must include at least one payment on or after 2019-01-01 to obtain present value outputs. Earlier payments are ignored, and discount factors align to the fixed anchor month.
