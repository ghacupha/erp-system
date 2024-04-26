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
import io.github.erp.domain.WorkInProgressOutstandingReport;
import io.github.erp.domain.WorkInProgressOutstandingReportREPO;
import io.github.erp.repository.WorkInProgressOutstandingReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface InternalWIPOutstandingReportRepository extends
    WorkInProgressOutstandingReportRepository,
    JpaRepository<WorkInProgressOutstandingReport, Long>,
    JpaSpecificationExecutor<WorkInProgressOutstandingReport> {

    @Query("SELECT NEW io.github.erp.domain.WorkInProgressOutstandingReportREPO(" +
        "w.id, " +
        "w.sequenceNumber, " +
        "w.particulars, " +
        "d.dealerName as dealerName," +
        "s.paymentNumber AS instalmentTransactionNumber," +
        "s.paymentDate AS instalmentTransactionDate, " +
        "c.iso4217CurrencyCode, " +
        "w.instalmentAmount, " +
        "CAST(COALESCE(SUM(ta.transferAmount), 0.0) AS java.math.BigDecimal) AS totalTransferAmount, " +
        "w.instalmentAmount - (CAST(COALESCE(SUM(ta.transferAmount), 0.0) AS java.math.BigDecimal)) ) " +
        "FROM WorkInProgressRegistration w " +
           "JOIN Dealer d ON d.id = w.dealer.id " +
           "JOIN SettlementCurrency c ON c.id = w.settlementCurrency.id " +
           "JOIN Settlement s ON s.id = w.settlementTransaction.id " +
           "LEFT JOIN WorkInProgressTransfer ta ON ta.workInProgressRegistration.id = w.id " +
        "WHERE s.paymentDate <= :reportDate " +
            "AND (ta.transferDate IS NULL OR ta.transferDate <= :reportDate) " +
        "GROUP BY w.id, w.sequenceNumber, w.particulars, d.dealerName, s.paymentNumber, s.paymentDate, c.iso4217CurrencyCode, w.instalmentAmount")
    Page<WorkInProgressOutstandingReportREPO> findByReportDate(@Param("reportDate") LocalDate reportDate, Pageable pageable);

    @Query("SELECT NEW io.github.erp.domain.WorkInProgressOutstandingReportREPO(" +
        "w.id, " +
        "w.sequenceNumber, " +
        "w.particulars, " +
        "d.dealerName, " +
        "s.paymentNumber AS instalmentTransactionNumber," +
        "s.paymentDate AS instalmentTransactionDate, " +
        "c.iso4217CurrencyCode, " +
        "w.instalmentAmount, " +
        "CAST(COALESCE(SUM(ta.transferAmount), 0.0) AS java.math.BigDecimal) AS totalTransferAmount, " +
        "w.instalmentAmount - (CAST(COALESCE(SUM(ta.transferAmount), 0.0) AS java.math.BigDecimal)) ) " +
        "FROM WorkInProgressRegistration w " +
           "JOIN Dealer d ON d.id = w.dealer.id " +
           "JOIN SettlementCurrency c ON c.id = w.settlementCurrency.id " +
           "JOIN Settlement s ON s.id = w.settlementTransaction.id " +
           "LEFT JOIN WorkInProgressTransfer ta ON ta.workInProgressRegistration.id = w.id " +
        "WHERE s.paymentDate <= :reportDate " +
            "AND (ta.transferDate IS NULL OR ta.transferDate <= :reportDate) " +
            "AND w.id = :id " +
        "GROUP BY w.id, w.sequenceNumber, w.particulars, d.dealerName, s.paymentNumber, s.paymentDate, c.iso4217CurrencyCode, w.instalmentAmount")
    Optional<WorkInProgressOutstandingReportREPO> findByReportDate(@Param("reportDate") LocalDate reportDate, @Param("id") Long id);

}
