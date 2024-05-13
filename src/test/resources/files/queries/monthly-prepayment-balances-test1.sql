WITH RECURSIVE end_month_series AS (
    SELECT
        DATE_TRUNC('month', CAST('2024-01-01' AS DATE)) AS start_date,
        (DATE_TRUNC('month', CAST('2024-12-31' AS DATE)) + INTERVAL '1 month' - INTERVAL '1 day') AS end_date
    UNION ALL
    SELECT
        (DATE_TRUNC('month', ems.start_date) + INTERVAL '1 month') AS start_date,
        ((DATE_TRUNC('month', ems.start_date) + INTERVAL '2 months' - INTERVAL '1 day')) AS end_date -- TODO Review use of date casting
    FROM
        end_month_series ems
    WHERE
            ems.start_date < CAST('2024-12-31' AS DATE)  -- TODO set first day of the last month here
)
SELECT
    ems.end_date AS FiscalMonthEndDate,
    total_prepayment.TotalPrepaymentAmount,
    COALESCE(SUM(ps.amortised_amount), 0) AS TotalAmortisedAmount,
    (total_prepayment.TotalPrepaymentAmount - COALESCE(SUM(ps.amortised_amount), 0)) AS TotalOutstandingAmount,
    COUNT(DISTINCT ps.id) AS NumberOfPrepaymentAccounts
FROM
    end_month_series ems
        LEFT JOIN (
        SELECT
            p.id,
            COALESCE(p.prepayment_amount, 0) AS prepayment_amount,
            COALESCE(pa.prepayment_amount, 0) AS amortised_amount,
            COALESCE(p.prepayment_amount, 0) - COALESCE(pa.prepayment_amount, 0) AS outstanding_amount,
            pa.fiscal_month_id,
            s.payment_date
        FROM
            prepayment_account p
                LEFT JOIN
            settlement s ON s.id = p.prepayment_transaction_id
                LEFT JOIN
            prepayment_amortization pa ON p.id = pa.prepayment_account_id
    ) ps ON TRUE
        CROSS JOIN LATERAL (
        SELECT SUM(COALESCE(p.prepayment_amount, 0)) AS TotalPrepaymentAmount
        FROM prepayment_account p
                 LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id
        WHERE s.payment_date <= ems.end_date
        ) AS total_prepayment
WHERE
        ps.fiscal_month_id IN (
        SELECT fm.id
        FROM fiscal_month fm
        WHERE fm.end_date <= ems.end_date
    )
  AND (
        (ps.payment_date <= ems.end_date AND ps.payment_date IS NOT NULL)
        OR (ps.payment_date IS NULL AND ps.fiscal_month_id IS NOT NULL)
    )
GROUP BY
    ems.end_date, total_prepayment.TotalPrepaymentAmount
ORDER BY
    ems.end_date
