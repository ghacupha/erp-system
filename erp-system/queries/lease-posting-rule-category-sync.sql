-- Sync lease posting rules with TA rule debit/credit accounts and account categories.
--
-- This script aligns lease-specific posting rules with legacy TA rules by:
-- 1) Updating templates to use the TA rule debit/credit accounts.
-- 2) Updating posting rule debit/credit account categories to match the
--    selected transaction accounts.
-- 3) Normalizing leaseContractId conditions for the affected posting rules.

WITH ta_rules AS (
    SELECT lease_contract_id,
        debit_id,
        credit_id,
        'LEASE_INTEREST_ACCRUAL'::varchar AS event_type
    FROM talease_interest_accrual_rule
    UNION ALL
    SELECT lease_contract_id,
        debit_id,
        credit_id,
        'LEASE_INTEREST_PAID_TRANSFER'::varchar AS event_type
    FROM tainterest_paid_transfer_rule
    UNION ALL
    SELECT lease_contract_id,
        debit_id,
        credit_id,
        'LEASE_REPAYMENT'::varchar AS event_type
    FROM talease_repayment_rule
    UNION ALL
    SELECT lease_contract_id,
        debit_id,
        credit_id,
        'LEASE_LIABILITY_RECOGNITION'::varchar AS event_type
    FROM talease_recognition_rule
),
lease_rule_map AS (
    SELECT pr.id AS posting_rule_id,
        ta.lease_contract_id,
        ta.debit_id,
        ta.credit_id
    FROM trx_account_posting_rule pr
    JOIN trx_account_posting_rule_condition prc
        ON prc.posting_rule_id = pr.id
        AND prc.condition_key = 'leaseContractId'
        AND prc.condition_operator = 'EQUALS'
    JOIN ta_rules ta
        ON ta.lease_contract_id = prc.condition_value::bigint
        AND pr.event_type = ta.event_type
    WHERE pr.module = 'LEASE'
)
UPDATE trx_account_posting_rule_template template
SET debit_account_id = mapping.debit_id,
    credit_account_id = mapping.credit_id
FROM lease_rule_map mapping
WHERE template.posting_rule_id = mapping.posting_rule_id;

UPDATE trx_account_posting_rule pr
SET debit_account_type_id = debit_account.account_category_id,
    credit_account_type_id = credit_account.account_category_id
FROM lease_rule_map mapping
JOIN transaction_account debit_account
    ON debit_account.id = mapping.debit_id
JOIN transaction_account credit_account
    ON credit_account.id = mapping.credit_id
WHERE pr.id = mapping.posting_rule_id;

UPDATE trx_account_posting_rule_condition condition
SET condition_value = mapping.lease_contract_id::text
FROM lease_rule_map mapping
WHERE condition.posting_rule_id = mapping.posting_rule_id
    AND condition.condition_key = 'leaseContractId';
