package io.github.erp.internal.report.service;

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
import io.github.erp.erp.reports.SettlementBillerReportRequisitionDTO;
import io.github.erp.internal.report.ReportModel;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Service("settlementBillerReportRequisitionServiceJPQL")
public class SettlementBillerReportRequisitionServiceJPQL implements SettlementBillerReportRequisitionService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SettlementBillerReportRequisitionServiceJPQL.class);
    // private final SessionFactory sessionFactory;

    private final EntityManagerFactory emFactory;

    public SettlementBillerReportRequisitionServiceJPQL(final EntityManagerFactory entityManager) {
        this.emFactory = entityManager;
    }

    public ReportModel<List<SettlementBillerReportDTO>> generateReport(final SettlementBillerReportRequisitionDTO reportRequisition) {
        long requestStart = System.currentTimeMillis();

        ReportModel<List<SettlementBillerReportDTO>> resultModel = getSummary(reportRequisition, emFactory);

        log.info("Summary list with {} items, generated in {} ms",resultModel.getReportData().size(),System.currentTimeMillis() - requestStart);

        return resultModel;
    }

    private ReportModel<List<SettlementBillerReportDTO>> getSummary( final SettlementBillerReportRequisitionDTO reportRequisition, final EntityManagerFactory emFactory ) {

        EntityManager entityManager = emFactory.createEntityManager();

        TypedQuery<SettlementBillerReportDTO> query =
            entityManager.createQuery(
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
                    "r.timeOfRequisition " +
                    ") " +
                    "FROM SettlementRequisition r " +
                    "JOIN r.biller d " +
                    "JOIN r.currentOwner o " +
                    "JOIN r.nativeOwner no " +
                    "JOIN r.nativeDepartment nd " +
                    "JOIN r.settlementCurrency c " +
                    "WHERE ( r.biller.id = :billerId )",
                SettlementBillerReportDTO.class);

        query.setParameter("billerId", reportRequisition.getBillerId());

        // TODO Add where clause for time of requisition

        ReportModel<List<SettlementBillerReportDTO>> reportModel = new ReportModel<>();
        reportModel.sqlString(query);
        reportModel.setReportData(query.getResultList());

        entityManager.close();

        return reportModel;
    }
}
