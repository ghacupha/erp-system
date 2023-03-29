package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.2-SNAPSHOT
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
