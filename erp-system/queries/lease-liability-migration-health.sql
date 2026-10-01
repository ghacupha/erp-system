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

-- Review liability enumeration migration state after deploying the deferred constraints.
select table_name
from information_schema.tables
where table_schema = 'public'
  and table_name in ('liability_enumeration', 'present_value_enumeration', 'lease_payment_upload');

select conname as constraint_name,
       conrelid::regclass as table_name,
       confrelid::regclass as referenced_table
from pg_constraint
where conname in (
    'fk_liability_enumeration__ifrs16lease_contract_id',
    'fk_liability_enumeration__lease_payment_upload_id',
    'fk_present_value_enumeration__ifrs16lease_contract_id',
    'fk_present_value_enumeration__liability_enumeration_id'
)
order by conname;
