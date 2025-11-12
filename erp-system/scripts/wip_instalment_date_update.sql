--
-- Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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


-- These are just notes on how we intend to update the wip table to include
-- instalment-date column.
-- This is necessary in order to facilitate sorting because if you wanted such
-- a list to be sorted in order of transactional precedence it's going to be hard.
-- It was thought easier to have such a column even if the update is itself
-- internally managed, within the service. For now we just need to copy this info
-- From the settlement table and apply a copy to the wip registration table.
UPDATE work_in_progress_registration wpr
SET instalment_date = (
    SELECT s.payment_date
    FROM settlement s
    WHERE s.id = wpr.settlement_transaction_id
);
