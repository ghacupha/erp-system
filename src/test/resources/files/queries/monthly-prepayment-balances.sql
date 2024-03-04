WITH RECURSIVE end_month_series AS (
    SELECT DATE '2023-01-01'::timestamp AS end_date
    UNION ALL
    SELECT end_date + INTERVAL '1 month'
    FROM end_month_series
    WHERE end_date < DATE '2023-12-31'
)
SELECT
    ems.end_date AS FiscalMonthEndDate,
    COALESCE(SUM(ps.prepayment_amount), 0) AS TotalPrepaymentAmount,
    COALESCE(SUM(ps.amortised_amount), 0) AS TotalAmortisedAmount,
    COALESCE(SUM(ps.outstanding_amount), 0) AS TotalOutstandingAmount,
    COALESCE(SUM(ps.prepayment_item_count), 0) AS NumberOfPrepaymentAccounts
FROM
    end_month_series ems
        LEFT JOIN LATERAL (
        SELECT
            pa.fiscal_month_id,
            SUM(COALESCE(p.prepayment_amount, 0)) as prepayment_amount,
            SUM(COALESCE(pa.prepayment_amount, 0)) as amortised_amount,
            SUM(COALESCE(p.prepayment_amount, 0) - COALESCE(pa.prepayment_amount, 0)) as outstanding_amount,
            COUNT(DISTINCT p.id) as prepayment_item_count
        FROM
            prepayment_account p
                LEFT JOIN prepayment_amortization pa ON p.id = pa.prepayment_account_id
        WHERE
            NOT EXISTS (
                    SELECT 1
                    FROM settlement s
                    WHERE s.id = p.prepayment_transaction_id
                      AND s.payment_date > ems.end_date
                )
        GROUP BY
            pa.fiscal_month_id
        ) ps ON ps.fiscal_month_id = (
        SELECT fm.id
        FROM fiscal_month fm
        WHERE fm.end_date <= ems.end_date
        ORDER BY fm.end_date DESC LIMIT 1
    )
GROUP BY
    ems.end_date
ORDER BY
    ems.end_date;
