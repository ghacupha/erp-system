# Lease Posting Rule Configuration

## Story 1: Lease Accountant configures contract-specific posting rules

**Persona:** Lease accountant

**Steps:**
1. Navigate to the **Lease Posting Rule Config** screen.
2. Select the lease contract to configure.
3. Review the suggested debit/credit accounts pulled from the lease template.
4. Confirm the default `leaseContractId` condition and adjust if needed.
5. Add additional templates or conditions where required.
6. Save the posting rule.

**Expected outcome:**
A posting rule is created for the selected lease contract, including templates and conditions that drive the lease posting engine.

## Story 2: Lease accountant refines account categories

**Persona:** Lease accountant

**Steps:**
1. Open the Lease Posting Rule Config form.
2. Change the debit and credit account categories to match the ledger policy.
3. Save the rule.

**Expected outcome:**
The posting rule persists with the updated account category metadata and can be evaluated by the posting-rule engine.
