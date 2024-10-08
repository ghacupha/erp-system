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

SELECT
    r.id,
    description,
    time_of_requisition,
    requisition_number,
    s.iso_4217_currency_code,
    payment_amount,
    payment_status,
    d.dealer_name as biller,
    a.application_identity as current_owner,
    n.application_identity as native_owner,
    nd.dealer_name as native_department
FROM public.settlement_requisition r,
     public.settlement_currency s,
     public.dealer d,
     public.application_user a,
     public.application_user n,
     public.dealer nd
WHERE r.settlement_currency_id = s.id
  AND r.native_department_id = nd.id
  AND r.native_owner_id = n.id
  AND r.current_owner_id = a.id
  AND r.biller_id = d.id
  AND r.payment_status <> 'PROCESSED'
  AND time_of_requisition = (SELECT MAX(sr.time_of_requisition) FROM public.settlement_requisition sr WHERE sr.requisition_number = r.requisition_number )
ORDER BY r.time_of_requisition DESC, r.requisition_number;
