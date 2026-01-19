# Lease Posting Rule Template Suggestions

## Persona
Lease accounting analyst configuring posting rules for IFRS 16 leases.

## Story: Event-aware template suggestions
**Scenario:** The analyst selects a lease contract and event type while setting up a posting rule.

**Steps**
1. Navigate to **Lease Posting Rule Config**.
2. Choose a lease contract.
3. Select an event type (for example, **LEASE_REPAYMENT** or **LEASE_INTEREST_ACCRUAL**).

**Expected Outcome**
- The debit and credit accounts auto-fill from the lease template fields that match the selected event type.
- The account type fields update to match the selected accounts.

## Story: Clearing stale defaults
**Scenario:** The analyst switches to a lease contract without template defaults for the selected event type.

**Steps**
1. Select a contract that has template defaults and observe the auto-filled accounts.
2. Switch to a contract that has no template or missing event-specific accounts.

**Expected Outcome**
- The debit/credit and account type fields clear, indicating that no defaults are available.
- The analyst can choose accounts manually without risking stale values.
