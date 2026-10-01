--
-- Erp System - Mark X No 12 (Kadar Series) Server ver 1.9.0
-- Copyright © 2021 - 2026 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
