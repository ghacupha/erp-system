package io.github.erp.internal.service;

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
