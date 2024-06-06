--
-- Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

UPDATE prepayment_marshalling AS pm
SET first_amortization_period_id = ap.id
FROM (
         SELECT pm.id AS pm_id, fm.id AS fm_id
         FROM prepayment_marshalling AS pm
                  LEFT JOIN fiscal_month AS fm ON pm.first_fiscal_month_id = fm.id
     ) AS subquery
         JOIN amortization_period AS ap ON subquery.fm_id = ap.fiscal_month_id
WHERE pm.id = subquery.pm_id;

UPDATE prepayment_amortization AS pa
SET amortization_period_id = ap.id
FROM fiscal_month AS fm
         JOIN amortization_period AS ap ON ap.fiscal_month_id = fm.id
WHERE pa.fiscal_month_id = fm.id;
