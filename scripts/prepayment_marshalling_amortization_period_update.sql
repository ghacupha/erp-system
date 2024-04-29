UPDATE prepayment_marshalling AS pm
SET first_amortization_period_id = ap.id
FROM (
         SELECT pm.id AS pm_id, fm.id AS fm_id
         FROM prepayment_marshalling AS pm
                  LEFT JOIN fiscal_month AS fm ON pm.first_fiscal_month_id = fm.id
     ) AS subquery
         JOIN amortization_period AS ap ON subquery.fm_id = ap.fiscal_month_id
WHERE pm.id = subquery.pm_id;
