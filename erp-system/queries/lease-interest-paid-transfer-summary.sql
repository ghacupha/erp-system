-- Native query backing the lease interest paid transfer summary endpoint.
-- Filters the target lease period, joins the lease contract, dealer and
-- transaction accounts used for the debit/credit booking and aggregates the
-- interest payment amount per contract. Rows where the aggregated amount is
-- zero are dropped to keep the report concise.
SELECT
    COALESCE(contract.booking_id, '') AS leaseId,
    COALESCE(d.dealer_name, '') AS dealerName,
    TRIM(BOTH ' ' FROM COALESCE(contract.booking_id, '') || ' ' || COALESCE(contract.lease_title, '')) AS narration,
    credit.account_number AS creditAccount,
    debit.account_number AS debitAccount,
    SUM(COALESCE(llsi.interest_payment, 0)) AS interestAmount
FROM lease_liability_schedule_item llsi
JOIN ifrs16lease_contract contract ON contract.id = llsi.lease_contract_id
LEFT JOIN dealer d ON contract.main_dealer_id = d.id
LEFT JOIN tainterest_paid_transfer_rule rule ON rule.lease_contract_id = contract.id
LEFT JOIN transaction_account debit ON debit.id = rule.debit_id
LEFT JOIN transaction_account credit ON credit.id = rule.credit_id
WHERE llsi.lease_period_id = :leasePeriodId
GROUP BY contract.booking_id, contract.lease_title, d.dealer_name, credit.account_number, debit.account_number
HAVING SUM(COALESCE(llsi.interest_payment, 0)) <> 0
ORDER BY contract.booking_id;
