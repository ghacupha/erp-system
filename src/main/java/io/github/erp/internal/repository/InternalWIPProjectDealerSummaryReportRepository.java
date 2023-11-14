package io.github.erp.internal.repository;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.WorkInProgressRegistration;
import io.github.erp.domain.WorkInProgressReport;
import io.github.erp.domain.WorkInProgressReportREPO;
import io.github.erp.domain.WorkProjectRegister;
import io.github.erp.repository.WorkInProgressReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface InternalWIPProjectDealerSummaryReportRepository extends
    WorkInProgressReportRepository,
    JpaRepository<WorkInProgressReport, Long>,
    JpaSpecificationExecutor<WorkInProgressReport> {

//    @Query("SELECT " +
//        "d.id, " +
//        "wr.projectTitle AS projectTitle, " +
//        "d.dealerName AS dealerName, " +
//        "COUNT(CASE WHEN st.paymentDate <= :reportDate THEN 1 ELSE NULL END) AS numberOfItems, " +
//        "CAST(SUM(w.instalmentAmount) AS java.math.BigDecimal) AS totalInstalmentAmount, " +
//        "CAST(SUM(COALESCE(ta.transferAmount, 0.0)) AS java.math.BigDecimal) AS totalTransferAmount, " +
//        "CAST(SUM(w.instalmentAmount) - SUM(COALESCE(ta.transferAmount, 0.0)) AS java.math.BigDecimal) AS outstandingAmount " +
//        "FROM WorkInProgressRegistration r " +
//        "LEFT JOIN Dealer d ON d.id = w.dealer.id " +
//        "LEFT JOIN WorkProjectRegister wr ON wr.id = r.workProjectRegister.id " +
//        "LEFT JOIN WorkInProgressTransfer ta ON ta.workInProgressRegistration.id = w.id " +
//        "LEFT JOIN Settlement st ON w.settlementTransaction.id = st.id " +
//        "WHERE st.paymentDate <= :reportDate " +
//        "AND w.settlementTransaction.paymentDate <= :reportDate " +
//        "GROUP BY wr.id, wr.projectTitle, d.id, d.dealerName " +
//        "ORDER BY wr.projectTitle, d.dealerName")
//    Page<WorkInProgressReportREPO> findSummaryByReportDate(@Param("reportDate") LocalDate reportDate, Pageable pageable);

    @Query("SELECT " +
        "wr.projectTitle AS projectTitle, " +
        "d.dealerName AS dealerName, " +
        "COUNT(CASE WHEN st.paymentDate <= :reportDate THEN 1 ELSE NULL END) AS numberOfItems, " +
        "CAST(SUM(r.instalmentAmount) AS java.math.BigDecimal) AS instalmentAmount, " +
        "CAST(SUM(COALESCE(ta.transferAmount, 0.0)) AS java.math.BigDecimal) AS transferAmount, " +
        "CAST(SUM(r.instalmentAmount) - SUM(COALESCE(ta.transferAmount, 0.0)) AS java.math.BigDecimal) AS outstandingAmount " +
        "FROM WorkInProgressRegistration r " +
        "JOIN Dealer d ON  r.dealer.id = d.id " +
        "JOIN WorkProjectRegister wr ON r.workProjectRegister.id = wr.id " +
        "JOIN WorkInProgressTransfer ta ON ta.workInProgressRegistration.id = r.id " +
        "JOIN Settlement st ON r.settlementTransaction.id = st.id " +
        "WHERE st.paymentDate <= :reportDate " +
        "GROUP BY wr.projectTitle, d.dealerName " +
        "ORDER BY wr.projectTitle, d.dealerName")
    Page<WorkInProgressReportREPO> findAllByReportDate(@Param("reportDate") LocalDate reportDate, Pageable pageable);

}
