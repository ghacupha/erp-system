package io.github.erp.internal.service;

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
import io.github.erp.IntegrationTest;
import io.github.erp.internal.report.service.SettlementBillerReportDTO;
import io.github.erp.erp.reports.SettlementBillerReportRequisitionDTO;
import io.github.erp.internal.report.ReportModel;
import io.github.erp.internal.report.service.SettlementBillerReportRequisitionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@IntegrationTest
public class SettlementBillerReportRequisitionServiceJPQLIT {

    private static final String RESULT_QUERY =
        "SELECT NEW io.github.erp.internal.report.service.SettlementBillerReportDTO ( " +
            "r.id, " +
            "d.dealerName, " +
            "r.description, " +
            "r.requisitionNumber, " +
            "c.iso4217CurrencyCode, " +
            "r.paymentAmount, " +
            "r.paymentStatus, " +
            "o.applicationIdentity, " +
            "no.applicationIdentity, " +
            "nd.dealerName, " +
            "r.timeOfRequisition ) " +
            "FROM SettlementRequisition r " +
            "JOIN r.biller d " +
            "JOIN r.currentOwner o " +
            "JOIN r.nativeOwner no " +
            "JOIN r.nativeDepartment nd " +
            "JOIN r.settlementCurrency c " +
            "WHERE " +
            "( r.biller.id = :billerId )";

    @Autowired
    @Qualifier("settlementBillerReportRequisitionServiceJPQL")
    private SettlementBillerReportRequisitionService reportModel;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void sessionFactoryIsInjected() {
        Assertions.assertThat(reportModel).isNotNull();
    }

    @Test
    @Transactional
    public void generateReport() throws Exception {
        ReportModel<List<SettlementBillerReportDTO>> reportSummaries = reportModel.generateReport(new SettlementBillerReportRequisitionDTO(1494, ZonedDateTime.now(), ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()) ));

        Assertions.assertThat(reportSummaries.getReportQuery()).isEqualTo(RESULT_QUERY);
    }
}
