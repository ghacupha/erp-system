package io.github.erp.internal.repository;

import io.github.erp.domain.WorkInProgressRegistration;
import io.github.erp.domain.WorkInProgressReportREPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface InternalWIPProjectDealerSummaryReportRepository extends
    JpaRepository<WorkInProgressRegistration, Long>,
    JpaSpecificationExecutor<WorkInProgressRegistration> {

    @Query("SELECT NEW io.github.erp.domain.WorkInProgressReportREPO(" +
        "w.workProjectRegister.projectTitle, " +
        "d.dealerName, " +
        "COUNT(CASE WHEN st.paymentDate <= :reportDate THEN 1 ELSE NULL END), " +
        "CAST(SUM(w.instalmentAmount) AS java.math.BigDecimal) AS totalInstalmentAmount, " +
        "CAST(SUM(COALESCE(ta.transferAmount, 0.0)) AS java.math.BigDecimal) AS totalTransferAmount, " +
        "CAST(SUM(w.instalmentAmount) - SUM(COALESCE(ta.transferAmount, 0.0)) AS java.math.BigDecimal) AS outstandingAmount " +
        ") " +
        "FROM WorkInProgressRegistration w " +
        "JOIN Dealer d ON d.id = w.dealer.id " +
        "JOIN WorkProjectRegister wr ON wr.id = w.workProjectRegister.id " +
        "LEFT JOIN WorkInProgressTransfer ta ON ta.workInProgressRegistration.id = w.id " +
        "LEFT JOIN Settlement st ON w.settlementTransaction.id = st.id " +
        "WHERE st.paymentDate <= :reportDate " +
        "GROUP BY w.workProjectRegister.projectTitle, w.id, d.dealerName " +
        "ORDER BY w.workProjectRegister.projectTitle")
    Page<WorkInProgressReportREPO> findSummaryByReportDate(@Param("reportDate") LocalDate reportDate, Pageable pageable);

}
