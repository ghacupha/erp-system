# Over-amortised prepayment account report

## Purpose

This report lists prepayment accounts whose active amortisation rows exceed the recognised prepayment amount.

## Query shape

The report groups amortisation rows by prepayment account and compares the accumulated amortised amount against the original prepayment amount. Only accounts with an excess balance are returned.

## Why it matters

Unlike the unallocated prepayment account report, this view is not tied to a reporting month. A prepayment account can become over-amortised at any time if duplicate amortisation activity is created or if entries are loaded incorrectly.
