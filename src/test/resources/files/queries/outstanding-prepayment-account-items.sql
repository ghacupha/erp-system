-- SELECT
--     p.id,
--     ta.account_name as PrepaymentAccount,
--     c.iso_4217_currency_code as currency,
--     COALESCE(p.prepayment_amount, 0) as prepayment_amount,
--     COALESCE(SUM(pa.prepayment_amount), 0) as amortised_amount,
--     COALESCE(p.prepayment_amount, 0) - COALESCE(SUM(pa.prepayment_amount), 0) as outstanding_amount
-- FROM prepayment_account p
--          LEFT JOIN transaction_account ta ON ta.id = p.debit_account_id
--          LEFT JOIN dealer d ON d.id = p.dealer_id
--          LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id
--          LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id
--          LEFT JOIN prepayment_amortization pa ON p.id = pa.prepayment_account_id AND pa.fiscal_month_id IN (
--     SELECT fm.id
--     FROM fiscal_month fm
--     WHERE fm.end_date <= '2023-10-31'
-- )
-- WHERE s.payment_date <= '2023-10-31' OR p.id NOT IN (
--     SELECT prep.id
--     FROM prepayment_account prep
--              LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id
--     WHERE s.payment_date > '2023-10-31'
-- )
-- GROUP BY
--     p.id,
--     ta.account_name,
--     c.iso_4217_currency_code;

SELECT
    ps.acc_id,
    ps.account_name,
    SUM(ps.prepayment_amount),
    SUM(ps.amortised_amount),
    SUM(ps.outstanding_amount),
    SUM(ps.prepayment_item_count),
    SUM(ps.amortised_item_count)
FROM (SELECT
          ta.id as acc_id,
          p.id,
          ta.account_name as account_name,
          c.iso_4217_currency_code as currency,
          COALESCE(p.prepayment_amount, 0) as prepayment_amount,
          COALESCE(SUM(pa.prepayment_amount), 0) as amortised_amount,
          COALESCE(p.prepayment_amount, 0) - COALESCE(SUM(pa.prepayment_amount), 0) as outstanding_amount,
          COALESCE(COUNT(DISTINCT p.id), 0) as prepayment_item_count,
          COALESCE(COUNT(ta.id), 0) as amortised_item_count
      FROM transaction_account ta
               JOIN prepayment_account p ON ta.id = p.debit_account_id
               LEFT JOIN dealer d ON d.id = p.dealer_id
               LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id
               LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id
               LEFT JOIN prepayment_amortization pa ON p.id = pa.prepayment_account_id AND pa.fiscal_month_id IN (
          SELECT fm.id
          FROM fiscal_month fm
          WHERE fm.end_date <= '2023-10-31'
      )
      WHERE s.payment_date <= '2023-10-31' OR p.id NOT IN (
          SELECT prep.id
          FROM prepayment_account prep
                   LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id
          WHERE s.payment_date > '2023-10-31'
      )
      GROUP BY
          p.id,
          ta.id,
          ta.account_name,
          c.iso_4217_currency_code,
          p.prepayment_amount
     ) ps

GROUP BY ps.account_name, ps.acc_id

