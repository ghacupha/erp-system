# Present value anchor date update

## Context
Lease liability present value calculations now anchor to the earliest relevant payment date instead of always using **2019-01-01**. The 2019 hard stop is only applied for contracts that actually have instalments dated before that threshold; otherwise the first payment date sets the valuation start point.

## Behaviour
- The calculator derives an anchor date from the earliest payment with a date; if that date precedes 2019-01-01 the anchor remains 2019-01-01, otherwise the first payment date is used.
- Payments before the chosen anchor are excluded from the computation, and sequencing begins from the anchor month without padding months when the anchor matches the first payment date.
- If no payments fall on or after the computed anchor, the calculator raises a descriptive `IllegalArgumentException`.

## Impact
Contracts whose first instalment occurs on or after 2019-01-01 are enumerated from that actual start date, eliminating unnecessary zero rows in reports like the present value enumeration. Only leases with genuinely historical payments still use the 2019 cutoff.
