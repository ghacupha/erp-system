-- Unallocated prepayment amortisations
-- Prepayment amortisation entries that are not allocated to a prepayment account.

select
    pa.id as amortization_id,
    pa.description,
    pa.prepayment_period,
    pa.prepayment_amount,
    currency.iso_4217_currency_code as currency_code,
    debit.account_number as debit_account_number,
    debit.account_name as debit_account_name,
    credit.account_number as credit_account_number,
    credit.account_name as credit_account_name,
    fm.fiscal_month_code,
    ap.period_code as amortization_period_code,
    pcr.id as compilation_request_id
from prepayment_amortization pa
left join settlement_currency currency on currency.id = pa.settlement_currency_id
left join transaction_account debit on debit.id = pa.debit_account_id
left join transaction_account credit on credit.id = pa.credit_account_id
left join fiscal_month fm on fm.id = pa.fiscal_month_id
left join amortization_period ap on ap.id = pa.amortization_period_id
left join prepayment_compilation_request pcr on pcr.id = pa.prepayment_compilation_request_id
where pa.prepayment_account_id is null
  and coalesce(pa.inactive, false) = false
order by pa.prepayment_period desc, pa.id desc;
