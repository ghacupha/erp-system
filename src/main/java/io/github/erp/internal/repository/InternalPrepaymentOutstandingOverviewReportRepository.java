package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.PrepaymentAccountReportTuple;
import io.github.erp.domain.PrepaymentOutstandingOverviewReport;
import io.github.erp.domain.PrepaymentOutstandingOverviewReportTuple;
import io.github.erp.repository.PrepaymentOutstandingOverviewReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data SQL repository for the PrepaymentOutstandingOverviewReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalPrepaymentOutstandingOverviewReportRepository extends
    PrepaymentOutstandingOverviewReportRepository,
    JpaRepository<PrepaymentOutstandingOverviewReport, Long> {

    @Query(
        nativeQuery = true,
        value = "SELECT " +
            " SUM(ps.prepayment_amount) as TotalPrepaymentAmount, " +
            " SUM(ps.amortised_amount) as TotalAmortisedAmount, " +
            " SUM(ps.outstanding_amount) as TotalOutstandingAmount, " +
            " SUM(ps.prepayment_item_count) as NumberOfPrepaymentAccounts " +
            "FROM (SELECT " +
            "    ta.id as acc_id, " +
            "    p.id,   " +
            "    ta.account_name as account_name, " +
            "    COALESCE(p.prepayment_amount, 0) as prepayment_amount, " +
            "    COALESCE(SUM(pa.prepayment_amount), 0) as amortised_amount, " +
            "    COALESCE(p.prepayment_amount, 0) - COALESCE(SUM(pa.prepayment_amount), 0) as outstanding_amount, " +
            "    COALESCE(COUNT(DISTINCT p.id), 0) as prepayment_item_count, " +
            "    COALESCE(COUNT(ta.id), 0) as amortised_item_count " +
            "FROM transaction_account ta   " +
            "  JOIN prepayment_account p ON ta.id = p.debit_account_id " +
            "  LEFT JOIN dealer d ON d.id = p.dealer_id " +
            "  LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id " +
            "  LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id " +
            "  LEFT JOIN prepayment_amortization pa ON p.id = pa.prepayment_account_id AND pa.fiscal_month_id IN ( " +
            "    SELECT fm.id " +
            "    FROM fiscal_month fm   " +
            "    WHERE fm.end_date <= :reportDate   " +
            ")   " +
            "WHERE p.recognition_date <= :reportDate OR p.id NOT IN (   " +
            "    SELECT prep.id   " +
            "    FROM prepayment_account prep " +
            "             LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id " +
            "    WHERE prep.recognition_date > :reportDate   " +
            ")   " +
            " GROUP BY   " +
            "    p.id,   " +
            "    ta.id,   " +
            "    ta.account_name,   " +
            "    c.iso_4217_currency_code,   " +
            "    p.prepayment_amount   " +
            ") ps   "
    )
    Optional<PrepaymentOutstandingOverviewReportTuple> findOneByReportDate(@Param("reportDate") LocalDate reportDate);

}
