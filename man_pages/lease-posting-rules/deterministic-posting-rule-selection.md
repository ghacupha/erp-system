# Deterministic Posting Rule Selection

## Overview
Lease postings rely on transaction account posting rules to decide which debit and credit accounts are applied for a given module/event pair. The posting rule evaluator previously selected the first matching rule returned by the repository, which could vary if multiple rules matched the same context. This change enforces deterministic behavior by ordering candidate rules and rejecting multiple matches outright, preventing silent mis-postings and ensuring configuration errors are surfaced early.

## Business Context
Posting rule overlaps are configuration issues: when two rules match the same context, the system can emit different accounting entries depending on repository ordering or data changes. That nondeterminism is especially risky for financial postings because it can lead to inconsistent debit/credit selection for the same transaction across runs.

## Technical Changes
- The posting rule repository now returns rules in a deterministic order (by ID ascending) so evaluation is stable.
- The evaluator collects all matching rules and raises an error if more than one match is found, including identifiers to help administrators resolve the overlap.
- The evaluator still fails fast when no matching rules exist or when a rule has no templates, preserving existing guardrails.

## Operational Guidance
Administrators should ensure only one posting rule matches a given module/event/context combination. If an overlap is detected, the posting run will fail with a clear error message so the rules can be corrected before any postings are persisted.
