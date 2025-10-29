-- Native query backing the lease liability outstanding summary endpoint.
SELECT COALESCE(contract.booking_id, '') AS lease_id,
       COALESCE(d.dealer_name, '') AS dealer_name,
       liability_account.account_number AS liability_account,
       interest_account.account_number AS interest_payable_account,
       SUM(COALESCE(llsi.outstanding_balance, 0)) AS lease_principal,
       SUM(COALESCE(llsi.interest_payable_closing, 0)) AS interest_payable
FROM lease_liability_schedule_item llsi
JOIN ifrs16lease_contract contract ON contract.id = llsi.lease_contract_id
LEFT JOIN dealer d ON contract.main_dealer_id = d.id
LEFT JOIN talease_recognition_rule recognition_rule ON recognition_rule.lease_contract_id = contract.id
LEFT JOIN transaction_account liability_account ON liability_account.id = recognition_rule.credit_id
LEFT JOIN talease_interest_accrual_rule accrual_rule ON accrual_rule.lease_contract_id = contract.id
LEFT JOIN transaction_account interest_account ON interest_account.id = accrual_rule.credit_id
WHERE llsi.lease_period_id = :leasePeriodId
GROUP BY contract.booking_id, d.dealer_name, liability_account.account_number, interest_account.account_number
HAVING SUM(COALESCE(llsi.outstanding_balance, 0)) <> 0
    OR SUM(COALESCE(llsi.interest_payable_closing, 0)) <> 0
ORDER BY contract.booking_id;
