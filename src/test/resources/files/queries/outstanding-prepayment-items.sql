SELECT
    p.catalogue_number as catalogue_number,
    p.particulars,
    d.dealer_name as dealer,
    s.payment_number as prepayment_trx,
    s.payment_date as prepayment_date,
    c.iso_4217_currency_code as currency,
    COALESCE(p.prepayment_amount, 0) as prepayment_amount,
    COALESCE(SUM(pa.prepayment_amount), 0) as amortised_amount,
    COALESCE(p.prepayment_amount, 0) - COALESCE(SUM(pa.prepayment_amount), 0) as outstanding_amount
FROM prepayment_account p
         LEFT JOIN dealer d ON d.id = p.dealer_id
         LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id
         LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id
         LEFT JOIN prepayment_amortization pa ON p.id = pa.prepayment_account_id AND pa.fiscal_month_id IN (
    SELECT fm.id
    FROM fiscal_month fm
    WHERE fm.end_date <= '2023-11-30'
)
WHERE s.payment_date <= '2023-11-30' OR p.id NOT IN (
    SELECT prep.id
    FROM prepayment_account prep
             LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id
    WHERE s.payment_date > '2023-11-30'
)
GROUP BY
    p.catalogue_number,
    p.particulars,
    d.dealer_name,
    s.payment_number,
    s.payment_date,
    c.iso_4217_currency_code,
    p.prepayment_amount;


