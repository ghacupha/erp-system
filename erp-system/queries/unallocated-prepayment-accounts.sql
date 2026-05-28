-- Unallocated prepayment accounts
-- Default materiality threshold: 1.00.
-- Increase the threshold when reviewing only large differences.

select
    p.id as prepayment_account_id,
    p.catalogue_number,
    p.particulars,
    p.recognition_date,
    d.dealer_name,
    debit.account_number as debit_account_number,
    debit.account_name as debit_account_name,
    transfer.account_number as transfer_account_number,
    transfer.account_name as transfer_account_name,
    currency.iso_4217_currency_code as currency_code,
    coalesce(p.prepayment_amount, 0) as prepayment_amount,
    coalesce(sum(pa.prepayment_amount), 0) as amortised_amount,
    coalesce(p.prepayment_amount, 0) - coalesce(sum(pa.prepayment_amount), 0) as outstanding_amount,
    count(pa.id) as amortization_entry_count,
    max(pa.prepayment_period) as last_amortization_date
from prepayment_account p
left join prepayment_amortization pa
    on pa.prepayment_account_id = p.id
    and coalesce(pa.inactive, false) = false
left join dealer d
    on d.id = p.dealer_id
left join transaction_account debit
    on debit.id = p.debit_account_id
left join transaction_account transfer
    on transfer.id = p.transfer_account_id
left join settlement_currency currency
    on currency.id = p.settlement_currency_id
group by
    p.id,
    p.catalogue_number,
    p.particulars,
    p.recognition_date,
    d.dealer_name,
    debit.account_number,
    debit.account_name,
    transfer.account_number,
    transfer.account_name,
    currency.iso_4217_currency_code,
    p.prepayment_amount
having coalesce(p.prepayment_amount, 0) - coalesce(sum(pa.prepayment_amount), 0) >= 1.00
order by outstanding_amount desc, p.catalogue_number asc;
