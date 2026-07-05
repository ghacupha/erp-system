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

-- Sample verification query for the ROU Asset Balance by Account report
-- Bind the :lease_period_id parameter with the identifier supplied in the
-- /api/leases/rou-account-balance-report-items/reports/{leasePeriodId} request.

SELECT
    rabi.id,
    rabi.asset_account_number,
    rabi.asset_account_name,
    rabi.opening_balance,
    rabi.depreciation_for_period,
    rabi.closing_balance,
    rabi.lease_period_id
FROM
    rou_account_balance_report_item rabi
WHERE
    rabi.lease_period_id = :lease_period_id
ORDER BY
    rabi.asset_account_number;
