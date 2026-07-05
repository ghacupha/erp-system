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

-- Native query backing the lease interest paid transfer summary endpoint.
-- Filters the target lease period, joins the lease contract, dealer and
-- transaction accounts used for the debit/credit booking and aggregates the
-- interest payment amount per contract. Rows where the aggregated amount is
-- zero are dropped to keep the report concise.
SELECT
    COALESCE(contract.booking_id, '') AS leaseId,
    COALESCE(d.dealer_name, '') AS dealerName,
    TRIM(BOTH ' ' FROM COALESCE(contract.booking_id, '') || ' ' || COALESCE(contract.lease_title, '')) AS narration,
    credit.account_number AS creditAccount,
    debit.account_number AS debitAccount,
    SUM(COALESCE(llsi.interest_payment, 0)) AS interestAmount
FROM lease_liability_schedule_item llsi
JOIN ifrs16lease_contract contract ON contract.id = llsi.lease_contract_id
LEFT JOIN dealer d ON contract.main_dealer_id = d.id
LEFT JOIN tainterest_paid_transfer_rule rule ON rule.lease_contract_id = contract.id
LEFT JOIN transaction_account debit ON debit.id = rule.debit_id
LEFT JOIN transaction_account credit ON credit.id = rule.credit_id
WHERE llsi.lease_period_id = :leasePeriodId
GROUP BY contract.booking_id, contract.lease_title, d.dealer_name, credit.account_number, debit.account_number
HAVING SUM(COALESCE(llsi.interest_payment, 0)) <> 0
ORDER BY contract.booking_id;
