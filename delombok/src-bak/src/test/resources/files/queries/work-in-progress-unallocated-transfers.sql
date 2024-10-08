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
    ta.id,
    ta.transfer_date,
    ta.transfer_type,
    ta.description,
    d.dealer_name,
    ta.transfer_amount
FROM work_in_progress_transfer ta
         JOIN settlement s ON s.id = ta.settlement_id
         JOIN dealer d ON d.id = s.biller_id
WHERE ta.transfer_date <= '2014-12-31'
  AND ta.work_in_progress_registration_id IS NULL
