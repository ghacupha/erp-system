## Using lease templates to configure lease repayment posting rules

Lease repayment postings now use the posting rule engine. Configure them by creating a posting rule with the event type `LEASE_REPAYMENT`.

1. Open the posting rule administration page.
2. Create a rule for module `LEASE` and event `LEASE_REPAYMENT`.
3. Add a posting template and select the debit and credit accounts.
4. If you use lease templates, reference the template's lease repayment accounts when selecting the debit and credit accounts.
5. Add optional conditions (for example, `leaseContractId`) and save the rule.

**Tip:** Legacy lease repayment rule forms are no longer required for posting. Use posting templates to define account mappings.
