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

-- Native query backing the lease liability outstanding summary endpoint.
SELECT COALESCE(contract.booking_id, '') AS lease_id,
       COALESCE(d.dealer_name, '') AS dealer_name,
       liability_account.account_number AS liability_account,
       interest_account.account_number AS interest_payable_account,
       SUM(COALESCE(llsi.outstanding_balance, 0)) AS lease_principal,
       SUM(COALESCE(llsi.interest_payable_closing, 0)) AS interest_payable
FROM lease_liability_schedule_item llsi
JOIN ifrs16lease_contract contract ON contract.id = llsi.lease_contract_id
LEFT JOIN dealer d ON contract.main_dealer_id = d.id
LEFT JOIN talease_recognition_rule recognition_rule ON recognition_rule.lease_contract_id = contract.id
LEFT JOIN transaction_account liability_account ON liability_account.id = recognition_rule.credit_id
LEFT JOIN talease_interest_accrual_rule accrual_rule ON accrual_rule.lease_contract_id = contract.id
LEFT JOIN transaction_account interest_account ON interest_account.id = accrual_rule.credit_id
WHERE llsi.lease_period_id = :leasePeriodId
GROUP BY contract.booking_id, d.dealer_name, liability_account.account_number, interest_account.account_number
HAVING SUM(COALESCE(llsi.outstanding_balance, 0)) <> 0
    OR SUM(COALESCE(llsi.interest_payable_closing, 0)) <> 0
ORDER BY contract.booking_id;
