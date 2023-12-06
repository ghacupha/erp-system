package io.github.erp.internal.repository;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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

//    @Query(value = "SELECT " +
//        "w.id, " +
//        "wipr.sequence_number, " +
//        "wipr.particulars, " +
//        "d.dealer_name, " +
//        "c.iso_4217_currency_code, " +
//        "COALESCE(wipr.total_instalment_amount, 0) as total_instalment_amount, " +
//        "COALESCE(wipt.total_transfer_amount, 0) AS total_transfer_amount, " +
//        "(COALESCE(wipr.total_instalment_amount, 0) - COALESCE(wipt.total_transfer_amount, 0)) AS total_outstanding_amount " +
//
//        "FROM (SELECT " +
//        "wp.id," +
//        "wp.sequence_number," +
//        "wp.particulars," +
//        "wp.dealer_id," +
//        "wp.settlement_transaction_id," +
//        "wp.settlement_currency_id," +
//        "SUM(wp.instalment_amount) as total_instalment_amount " +
//        "FROM work_in_progress_registration wp " +
//        "GROUP BY " +
//        "wp.id, " +
//        "wp.sequence_number, " +
//        "wp.particulars, " +
//        "wp.dealer_id, " +
//        "wp.settlement_currency_id, " +
//        "wp.settlement_transaction_id ) wipr " +
//
//        "JOIN work_in_progress_registration w ON w.id = wipr.id " +
//
//        "JOIN dealer d ON d.id = wipr.dealer_id " +
//
//        "LEFT JOIN settlement_currency c ON c.id = wipr.settlement_currency_id " +
//
//        "LEFT JOIN (SELECT " +
//        "wipr.id AS wipr_id, " +
//        "SUM(wpt.transfer_amount) AS total_transfer_amount " +
//        "FROM work_in_progress_registration wipr " +
//        "LEFT JOIN work_in_progress_transfer wpt ON wipr.id = wpt.work_in_progress_registration_id " +
//        "GROUP BY wipr.id) wipt ON wipt.wipr_id = wipr.id " +
//
//        "LEFT JOIN settlement s ON s.id = wipr.settlement_transaction_id " +
//
//        "WHERE s.payment_date <= :reportDate " +
//
//        "ORDER BY s.payment_date, wipr.id ASC",
//
//        countQuery = "SELECT COUNT(wp.id) FROM work_in_progress_registration wp",
//
//        nativeQuery = true)
//    Page<WorkInProgressOutstandingReportTuple> findByReportDate(@Param("reportDate") LocalDate reportDate, Pageable pageable);



    @Query("SELECT NEW io.github.erp.domain.WorkInProgressOutstandingReportREPO(" +
        "w.id, " +
        "w.sequenceNumber, " +
        "w.particulars, " +
        "d.dealerName, " +
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
        "GROUP BY w.id, w.sequenceNumber, w.particulars, d.dealerName, c.iso4217CurrencyCode, w.instalmentAmount")
    Page<WorkInProgressOutstandingReportREPO> findByReportDate(@Param("reportDate") LocalDate reportDate, Pageable pageable);

    @Query("SELECT NEW io.github.erp.domain.WorkInProgressOutstandingReportREPO(" +
        "w.id, " +
        "w.sequenceNumber, " +
        "w.particulars, " +
        "d.dealerName, " +
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
        "GROUP BY w.id, w.sequenceNumber, w.particulars, d.dealerName, c.iso4217CurrencyCode, w.instalmentAmount")
    Optional<WorkInProgressOutstandingReportREPO> findByReportDate(@Param("reportDate") LocalDate reportDate, @Param("id") Long id);

}
