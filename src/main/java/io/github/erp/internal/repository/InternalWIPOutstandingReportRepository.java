package io.github.erp.internal.repository;

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

public interface InternalWIPOutstandingReportRepository extends
    WorkInProgressOutstandingReportRepository,
    JpaRepository<WorkInProgressOutstandingReport, Long>,
    JpaSpecificationExecutor<WorkInProgressOutstandingReport> {

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

}
