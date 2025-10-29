package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.domain.LeaseLiabilityScheduleReportItem;
import io.github.erp.internal.model.LeaseInterestPaidTransferSummaryInternal;
import io.github.erp.internal.model.LeaseLiabilityInterestExpenseSummaryInternal;
import io.github.erp.internal.model.LeaseLiabilityMaturitySummaryInternal;
import io.github.erp.internal.model.LeaseLiabilityOutstandingSummaryInternal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LeaseLiabilityScheduleReportItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalLeaseLiabilityScheduleReportItemRepository
    extends JpaRepository<LeaseLiabilityScheduleReportItem, Long>, JpaSpecificationExecutor<LeaseLiabilityScheduleReportItem> {

    @Query(
        value =
            "WITH target_period AS (\n" +
            "    SELECT lp.id AS target_period_id,\n" +
            "           lp.period_code AS target_period_code,\n" +
            "           CAST(NULLIF(lp.period_code, '') AS bigint) AS target_period_numeric,\n" +
            "           SUBSTRING(lp.period_code, 1, 4) AS target_year,\n" +
            "           lp.end_date AS target_end_date,\n" +
            "           fm.month_number AS target_month_number,\n" +
            "           fm.fiscal_year_id AS target_fiscal_year_id\n" +
            "    FROM lease_repayment_period lp\n" +
            "    LEFT JOIN fiscal_month fm ON lp.fiscal_month_id = fm.id\n" +
            "    WHERE lp.id = :leasePeriodId\n" +
            "), period_interest AS (\n" +
            "    SELECT llsi.lease_liability_id,\n" +
            "           llsi.lease_contract_id,\n" +
            "           SUM(COALESCE(llsi.interest_accrued, 0)) AS period_interest\n" +
            "    FROM lease_liability_schedule_item llsi\n" +
            "    WHERE llsi.lease_period_id = :leasePeriodId\n" +
            "    GROUP BY llsi.lease_liability_id, llsi.lease_contract_id\n" +
            "), annual_interest AS (\n" +
            "    SELECT llsi.lease_liability_id,\n" +
            "           llsi.lease_contract_id,\n" +
            "           SUM(COALESCE(llsi.interest_accrued, 0)) AS cumulative_interest\n" +
            "    FROM lease_liability_schedule_item llsi\n" +
            "    JOIN lease_repayment_period lp ON llsi.lease_period_id = lp.id\n" +
            "    LEFT JOIN fiscal_month fm ON lp.fiscal_month_id = fm.id\n" +
            "    CROSS JOIN target_period tp\n" +
            "    WHERE (\n" +
            "            (tp.target_fiscal_year_id IS NOT NULL AND fm.fiscal_year_id = tp.target_fiscal_year_id AND fm.month_number <= tp.target_month_number)\n" +
            "         OR (tp.target_fiscal_year_id IS NULL AND SUBSTRING(lp.period_code, 1, 4) = tp.target_year AND CAST(NULLIF(lp.period_code, '') AS bigint) <= tp.target_period_numeric)\n" +
            "    )\n" +
            "    GROUP BY llsi.lease_liability_id, llsi.lease_contract_id\n" +
            ")\n" +
            "SELECT ll.lease_id AS leaseNumber,\n" +
            "       d.dealer_name AS dealerName,\n" +
            "       COALESCE(contract.booking_id, '') || ' ' || COALESCE(contract.lease_title, '') AS narration,\n" +
            "       credit.account_number AS creditAccount,\n" +
            "       debit.account_number AS debitAccount,\n" +
            "       COALESCE(pi.period_interest, 0) AS interestExpense,\n" +
            "       COALESCE(ai.cumulative_interest, 0) AS cumulativeAnnual,\n" +
            "       (COALESCE(ai.cumulative_interest, 0) - COALESCE(pi.period_interest, 0)) AS cumulativeLastMonth\n" +
            "FROM period_interest pi\n" +
            "JOIN annual_interest ai ON ai.lease_liability_id = pi.lease_liability_id AND ai.lease_contract_id = pi.lease_contract_id\n" +
            "JOIN lease_liability ll ON ll.id = pi.lease_liability_id\n" +
            "JOIN ifrs16lease_contract contract ON contract.id = pi.lease_contract_id\n" +
            "LEFT JOIN dealer d ON contract.main_dealer_id = d.id\n" +
            "LEFT JOIN talease_interest_accrual_rule rule ON rule.lease_contract_id = contract.id\n" +
            "LEFT JOIN transaction_account debit ON debit.id = rule.debit_id\n" +
            "LEFT JOIN transaction_account credit ON credit.id = rule.credit_id\n" +
            "ORDER BY ll.lease_id",
        nativeQuery = true
    )
    List<LeaseLiabilityInterestExpenseSummaryInternal> getLeaseLiabilityInterestExpenseSummary(
        @Param("leasePeriodId") long leasePeriodId
    );

    @Query(
        value =
            "SELECT COALESCE(contract.booking_id, '') AS leaseId,\n" +
            "       COALESCE(d.dealer_name, '') AS dealerName,\n" +
            "       TRIM(BOTH ' ' FROM COALESCE(contract.booking_id, '') || ' ' || COALESCE(contract.lease_title, '')) AS narration,\n" +
            "       credit.account_number AS creditAccount,\n" +
            "       debit.account_number AS debitAccount,\n" +
            "       SUM(COALESCE(llsi.interest_payment, 0)) AS interestAmount\n" +
            "FROM lease_liability_schedule_item llsi\n" +
            "JOIN ifrs16lease_contract contract ON contract.id = llsi.lease_contract_id\n" +
            "LEFT JOIN dealer d ON contract.main_dealer_id = d.id\n" +
            "LEFT JOIN tainterest_paid_transfer_rule rule ON rule.lease_contract_id = contract.id\n" +
            "LEFT JOIN transaction_account debit ON debit.id = rule.debit_id\n" +
            "LEFT JOIN transaction_account credit ON credit.id = rule.credit_id\n" +
            "WHERE llsi.lease_period_id = :leasePeriodId\n" +
            "GROUP BY contract.booking_id, contract.lease_title, d.dealer_name, credit.account_number, debit.account_number\n" +
            "HAVING SUM(COALESCE(llsi.interest_payment, 0)) <> 0\n" +
            "ORDER BY contract.booking_id",
        nativeQuery = true
    )
    List<LeaseInterestPaidTransferSummaryInternal> getLeaseInterestPaidTransferSummary(
        @Param("leasePeriodId") long leasePeriodId
    );

    @Query(
        value =
            "SELECT COALESCE(contract.booking_id, '') AS leaseId,\n" +
            "       COALESCE(d.dealer_name, '') AS dealerName,\n" +
            "       liability_account.account_number AS liabilityAccount,\n" +
            "       interest_account.account_number AS interestPayableAccount,\n" +
            "       SUM(COALESCE(llsi.outstanding_balance, 0)) AS leasePrincipal,\n" +
            "       SUM(COALESCE(llsi.interest_payable_closing, 0)) AS interestPayable\n" +
            "FROM lease_liability_schedule_item llsi\n" +
            "JOIN ifrs16lease_contract contract ON contract.id = llsi.lease_contract_id\n" +
            "LEFT JOIN dealer d ON contract.main_dealer_id = d.id\n" +
            "LEFT JOIN talease_recognition_rule recognition_rule ON recognition_rule.lease_contract_id = contract.id\n" +
            "LEFT JOIN transaction_account liability_account ON liability_account.id = recognition_rule.credit_id\n" +
            "LEFT JOIN talease_interest_accrual_rule accrual_rule ON accrual_rule.lease_contract_id = contract.id\n" +
            "LEFT JOIN transaction_account interest_account ON interest_account.id = accrual_rule.credit_id\n" +
            "WHERE llsi.lease_period_id = :leasePeriodId\n" +
            "GROUP BY contract.booking_id, d.dealer_name, liability_account.account_number, interest_account.account_number\n" +
            "HAVING SUM(COALESCE(llsi.outstanding_balance, 0)) <> 0\n" +
            "    OR SUM(COALESCE(llsi.interest_payable_closing, 0)) <> 0\n" +
            "ORDER BY contract.booking_id",
        nativeQuery = true
    )
    List<LeaseLiabilityOutstandingSummaryInternal> getLeaseLiabilityOutstandingSummary(
        @Param("leasePeriodId") long leasePeriodId
    );

    @Query(
        value =
            "WITH target_period AS (\n" +
            "    SELECT lp.end_date AS target_end_date\n" +
            "    FROM lease_repayment_period lp\n" +
            "    WHERE lp.id = :leasePeriodId\n" +
            "), maturity_data AS (\n" +
            "    SELECT CASE\n" +
            "               WHEN maturity_days <= 365 THEN '≤365 days'\n" +
            "               WHEN maturity_days BETWEEN 366 AND 1824 THEN '366–1824 days'\n" +
            "               ELSE '≥1825 days'\n" +
            "           END AS maturity_label,\n" +
            "           COALESCE(llsi.outstanding_balance, 0) AS lease_principal,\n" +
            "           COALESCE(llsi.interest_payable_closing, 0) AS interest_payable\n" +
            "    FROM lease_liability_schedule_item llsi\n" +
            "    JOIN lease_liability ll ON ll.id = llsi.lease_liability_id\n" +
            "    CROSS JOIN target_period tp\n" +
            "    CROSS JOIN LATERAL (\n" +
            "        SELECT GREATEST(COALESCE(DATE_PART('day', ll.end_date - tp.target_end_date), 0), 0)::bigint AS maturity_days\n" +
            "    ) maturity\n" +
            "    WHERE llsi.lease_period_id = :leasePeriodId\n" +
            ")\n" +
            "SELECT maturity_label AS maturityLabel,\n" +
            "       SUM(lease_principal) AS leasePrincipal,\n" +
            "       SUM(interest_payable) AS interestPayable,\n" +
            "       SUM(lease_principal + interest_payable) AS total\n" +
            "FROM maturity_data\n" +
            "GROUP BY maturity_label\n" +
            "HAVING SUM(lease_principal) <> 0 OR SUM(interest_payable) <> 0\n" +
            "ORDER BY CASE maturity_label\n" +
            "            WHEN '≤365 days' THEN 1\n" +
            "            WHEN '366–1824 days' THEN 2\n" +
            "            ELSE 3\n" +
            "         END",
        nativeQuery = true
    )
    List<LeaseLiabilityMaturitySummaryInternal> getLeaseLiabilityMaturitySummary(
        @Param("leasePeriodId") long leasePeriodId
    );
}
