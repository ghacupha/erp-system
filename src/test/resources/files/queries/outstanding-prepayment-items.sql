SELECT
    p.catalogue_number as CatalogueNumber,
    p.particulars,
    d.dealer_name as Dealer,
    s.payment_number as PrepaymentTRX,
    s.payment_date as PrepaymentDate,
    c.iso_4217_currency_code as CCode,
    coalesce(prepaid.pa_amount, 0) as PrepaymentAmount,
    COALESCE(SUM(pa.prepayment_amount), 0) as AmortisedAmount,
    coalesce(prepaid.pa_amount, 0) - COALESCE(SUM(pa.prepayment_amount), 0) as OutstandingAmount
FROM prepayment_account p

LEFT JOIN dealer d ON d.id = p.dealer_id

LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id

LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id

LEFT JOIN (
    SELECT pa.prepayment_account_id, SUM(pa.prepayment_amount) as prepayment_amount
    FROM prepayment_amortization pa
             LEFT JOIN fiscal_month fm ON fm.id = pa.fiscal_month_id
    WHERE fm.end_date <= '2023-11-30'
    GROUP BY pa.prepayment_account_id
) pa ON p.id = pa.prepayment_account_id

LEFT JOIN (
    SELECT prep.id as prepayment_id, SUM(CASE WHEN s.payment_date <= '2023-11-30' THEN prep.prepayment_amount ELSE 0 END) as pa_amount
    FROM prepayment_account prep
             LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id
    WHERE s.payment_date <= '2023-11-30'
    GROUP BY prep.id
) prepaid ON p.id = prepaid.prepayment_id

WHERE s.payment_date <= '2023-11-30'

GROUP BY
    p.catalogue_number,
    p.particulars,
    d.dealer_name,
    s.payment_number,
    s.payment_date,
    c.iso_4217_currency_code,
    p.prepayment_amount,
    prepaid.pa_amount;

