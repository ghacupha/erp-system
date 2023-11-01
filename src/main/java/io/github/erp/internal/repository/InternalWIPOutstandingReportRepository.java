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

//    @Query("SELECT\n" +
//        "    w.id,\n" +
//        "    w.sequenceNumber,\n" +
//        "    w.particulars,\n" +
//        "    d.dealerName,\n" +
//        "    c.iso4217CurrencyCode,\n" +
//        "    w.instalmentAmount as im,\n" +
//        "    COALESCE(SUM(ta.transferAmount), 0) AS totalTransferAmount,\n" +
//        "    (w.instalmentAmount - COALESCE(SUM(ta.transferAmount), 0)) AS outstandingAmount\n" +
//        "FROM WorkInProgressRegistration w\n" +
//        "         JOIN Dealer d ON d.id = w.dealer.id\n" +
//        "         JOIN SettlementCurrency c ON c.id = w.settlementCurrency.id\n" +
//        "         JOIN Settlement s ON s.id = w.settlementTransaction.id\n" +
//        "         LEFT JOIN WorkInProgressTransfer ta ON ta.workInProgressRegistration.id = w.id\n" +
//        "WHERE s.paymentDate <= '2014-12-31'\n" +
//        "  AND (ta.transferDate IS NULL OR ta.transferDate <= '2014-12-31')\n" +
//        "GROUP BY w.id, w.sequenceNumber, w.particulars, d.dealerName, c.iso4217CurrencyCode, w.instalmentAmount\n")
//    Page<WorkInProgressOutstandingReport> findByReportDate(@Param("reportDate") String reportDate, Pageable pageable);

    @Query("SELECT NEW io.github.erp.domain.WorkInProgressOutstandingReportREPO(" +
        "w.id, " +
        "w.sequenceNumber, " +
        "w.particulars, " +
        "d.dealerName, " +
        "c.iso4217CurrencyCode, " +
        "w.instalmentAmount, " +
        "CAST(COALESCE(SUM(ta.transferAmount), 0.0) AS java.math.BigDecimal) AS totalTransferAmount, " +
        "w.instalmentAmount ) " +
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
