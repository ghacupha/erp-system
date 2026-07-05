## Using lease templates to configure interest paid transfer posting rules

Interest paid transfer postings now use the posting rule engine. Configure them by creating a posting rule with the event type `LEASE_INTEREST_PAID_TRANSFER`.

1. Open the posting rule administration page.
2. Create a rule for module `LEASE` and event `LEASE_INTEREST_PAID_TRANSFER`.
3. Add a posting template and select the debit and credit accounts.
4. If you use lease templates, reference the template's interest paid transfer accounts when selecting the debit and credit accounts.
5. Add optional conditions (for example, `leaseContractId`) and save the rule.

**Notes:** Legacy interest paid transfer rule forms are no longer required for posting. Use posting templates to define account mappings.
