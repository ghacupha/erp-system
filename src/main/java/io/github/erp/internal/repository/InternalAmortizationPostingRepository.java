package io.github.erp.internal.repository;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
