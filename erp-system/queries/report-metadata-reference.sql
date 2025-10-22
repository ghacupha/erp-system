-- Inspect active dynamic report metadata definitions.
SELECT
    rm.report_title,
    rm.module,
    rm.page_path,
    rm.backend_api,
    rm.display_lease_period,
    rm.display_lease_contract,
    COALESCE(rm.lease_period_query_param, 'leasePeriodId.equals')   AS lease_period_param,
    COALESCE(rm.lease_contract_query_param, 'leaseLiabilityId.equals') AS lease_contract_param
FROM report_metadata rm
WHERE rm.active = TRUE
ORDER BY rm.report_title;

-- Example: preview lease liability schedule aggregates for a metadata-driven report.
-- Replace :lease_period_id with the desired lease period identifier.
SELECT
    lli.transaction_account_id,
    SUM(lli.interest_accrued)        AS interest_accrued,
    SUM(lli.principal_payment)       AS principal_payment,
    SUM(lli.cash_payment)            AS cash_payment,
    SUM(lli.lease_liability_balance) AS lease_liability_balance
FROM lease_liability_schedule_item lli
WHERE lli.lease_period_id = :lease_period_id
GROUP BY lli.transaction_account_id
ORDER BY lli.transaction_account_id;
