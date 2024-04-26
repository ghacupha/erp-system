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
