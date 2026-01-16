# User Story: Resolve Overlapping Posting Rules

## Persona
**Finance Administrator** responsible for maintaining lease posting configuration.

## Scenario
The administrator wants to ensure that a lease posting run always uses a single, deterministic posting rule for a given module/event and that configuration overlaps are detected before postings are recorded.

## Steps
1. Open the posting rule administration page.
2. Review posting rules for a given module and event type.
3. Ensure only one rule matches the intended context (for example, avoid duplicate or overlapping conditions).
4. Run a lease posting action for the event.

## Expected Outcome
- If exactly one rule matches, the posting completes with the expected debit and credit accounts.
- If multiple rules match, the system rejects the posting with a clear error that identifies the conflicting rules so the administrator can resolve the overlap.
