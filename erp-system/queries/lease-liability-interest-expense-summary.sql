-- Lease liability interest expense summary report
-- Parameters:
--   :leasePeriodId - identifier of the lease repayment period driving the report
WITH target_period AS (
    SELECT lp.id AS target_period_id,
           lp.period_code AS target_period_code,
           CAST(NULLIF(lp.period_code, '') AS bigint) AS target_period_numeric,
           SUBSTRING(lp.period_code, 1, 4) AS target_year,
           lp.end_date AS target_end_date,
           fm.month_number AS target_month_number,
           fm.fiscal_year_id AS target_fiscal_year_id
    FROM lease_repayment_period lp
    LEFT JOIN fiscal_month fm ON lp.fiscal_month_id = fm.id
    WHERE lp.id = :leasePeriodId
), period_interest AS (
    SELECT llsi.lease_liability_id,
           llsi.lease_contract_id,
           SUM(COALESCE(llsi.interest_accrued, 0)) AS period_interest
    FROM lease_liability_schedule_item llsi
    WHERE llsi.lease_period_id = :leasePeriodId
    GROUP BY llsi.lease_liability_id, llsi.lease_contract_id
), annual_interest AS (
    SELECT llsi.lease_liability_id,
           llsi.lease_contract_id,
           SUM(COALESCE(llsi.interest_accrued, 0)) AS cumulative_interest
    FROM lease_liability_schedule_item llsi
    JOIN lease_repayment_period lp ON llsi.lease_period_id = lp.id
    LEFT JOIN fiscal_month fm ON lp.fiscal_month_id = fm.id
    CROSS JOIN target_period tp
    WHERE (
            tp.target_fiscal_year_id IS NOT NULL
            AND fm.fiscal_year_id = tp.target_fiscal_year_id
            AND fm.month_number <= tp.target_month_number
        )
        OR (
            tp.target_fiscal_year_id IS NULL
            AND SUBSTRING(lp.period_code, 1, 4) = tp.target_year
            AND CAST(NULLIF(lp.period_code, '') AS bigint) <= tp.target_period_numeric
        )
    GROUP BY llsi.lease_liability_id, llsi.lease_contract_id
)
SELECT ll.lease_id AS leaseNumber,
       d.dealer_name AS dealerName,
       COALESCE(contract.booking_id, '') || ' ' || COALESCE(contract.lease_title, '') AS narration,
       credit.account_number AS creditAccount,
       debit.account_number AS debitAccount,
       COALESCE(pi.period_interest, 0) AS interestExpense,
       COALESCE(ai.cumulative_interest, 0) AS cumulativeAnnual,
       (COALESCE(ai.cumulative_interest, 0) - COALESCE(pi.period_interest, 0)) AS cumulativeLastMonth
FROM period_interest pi
JOIN annual_interest ai ON ai.lease_liability_id = pi.lease_liability_id AND ai.lease_contract_id = pi.lease_contract_id
JOIN lease_liability ll ON ll.id = pi.lease_liability_id
JOIN ifrs16lease_contract contract ON contract.id = pi.lease_contract_id
LEFT JOIN dealer d ON contract.main_dealer_id = d.id
LEFT JOIN talease_interest_accrual_rule rule ON rule.lease_contract_id = contract.id
LEFT JOIN transaction_account debit ON debit.id = rule.debit_id
LEFT JOIN transaction_account credit ON credit.id = rule.credit_id
ORDER BY ll.lease_id;
