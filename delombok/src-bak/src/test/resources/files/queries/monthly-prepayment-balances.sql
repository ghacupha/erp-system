--
-- Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
-- Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program. If not, see <http://www.gnu.org/licenses/>.
--

WITH RECURSIVE end_month_series AS (
    SELECT
        DATE_TRUNC('month', '2023-01-01'::date) AS start_date,
        (DATE_TRUNC('month', '2023-01-01'::date) + INTERVAL '1 month' - INTERVAL '1 day') AS end_date
    UNION ALL
    SELECT
        (DATE_TRUNC('month', ems.start_date) + INTERVAL '1 month') AS start_date,
        ((DATE_TRUNC('month', ems.start_date) + INTERVAL '2 months' - INTERVAL '1 day')::date) AS end_date
    FROM
        end_month_series ems
    WHERE
            ems.start_date < '2023-12-01'::date  -- // TODO set first day of the last month here
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
    ems.end_date;
