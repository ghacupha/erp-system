package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.AmortizationPostingReport;
import io.github.erp.domain.AmortizationPostingReportInternal;
import io.github.erp.domain.WorkInProgressOutstandingReportREPO;
import io.github.erp.repository.AmortizationPostingReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface InternalAmortizationPostingRepository
    extends AmortizationPostingReportRepository,
    JpaRepository<AmortizationPostingReport, Long> {

    @Query(value = "SELECT " +
        " p.id  as id, " +
        " pa.catalogue_number as catalogueNumber," +
        " da.account_number as debitAccount," +
        " ca.account_number as creditAccount," +
        " description as description, " +
        " p.prepayment_amount as amortizationAmount  " +
        "FROM public.prepayment_amortization p  " +
        "LEFT JOIN prepayment_account pa on prepayment_account_id = pa.id " +
        "LEFT JOIN transaction_account da on p.debit_account_id = da.id  " +
        "LEFT JOIN transaction_account ca on p.credit_account_id = ca.id " +
        "LEFT JOIN fiscal_month fm on p.fiscal_month_id = fm.id " +
        "WHERE :reportDate BETWEEN fm.start_date AND fm.end_date", nativeQuery = true)
    Page<AmortizationPostingReportInternal> findByReportDate(@Param("reportDate") LocalDate reportDate, Pageable pageable);
}
