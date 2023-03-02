package io.github.erp.service.reports;

import io.github.erp.domain.*;
import io.github.erp.domain.enumeration.PaymentStatus;
import io.github.erp.erp.reports.SettlementBillerReportDTO;
import io.github.erp.erp.reports.SettlementBillerReportRequisitionDTO;
import io.github.erp.internal.report.ReportModel;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Transactional
@Service("settlementBillerReportRequisitionServiceJPQL")
public class SettlementBillerReportRequisitionServiceJPQL implements SettlementBillerReportRequisitionService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SettlementBillerReportRequisitionServiceJPQL.class);
    // private final SessionFactory sessionFactory;

    private final EntityManagerFactory emFactory;

    public SettlementBillerReportRequisitionServiceJPQL(final EntityManagerFactory entityManager) {
        // this.sessionFactory = entityManager.unwrap(SessionFactory.class);;
        this.emFactory = entityManager;
    }

    public ReportModel<List<SettlementBillerReportDTO>> generateReport(final SettlementBillerReportRequisitionDTO reportRequisition) {
        long requestStart = System.currentTimeMillis();

        ReportModel<List<SettlementBillerReportDTO>> resultModel = getSummary(reportRequisition, emFactory);

        log.info("Table F summary list with {} items, generated in {} ms",resultModel.getReportData().size(),System.currentTimeMillis() - requestStart);

        return resultModel;
    }

    private ReportModel<List<SettlementBillerReportDTO>> getSummary( final SettlementBillerReportRequisitionDTO reportRequisition, final EntityManagerFactory emFactory ) {

        EntityManager entityManager = emFactory.createEntityManager();

        TypedQuery<SettlementBillerReportDTO> query =
            entityManager.createQuery(
                "SELECT NEW io.github.erp.erp.reports.SettlementBillerReportDTO ( " +
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
                    "JOIN r.settlementCurrency c ",
                SettlementBillerReportDTO.class);

        // TODO Add where clause for biller_id
        // TODO Add where clause for time of requisition

        ReportModel<List<SettlementBillerReportDTO>> reportModel = new ReportModel<>();
        reportModel.sqlString(query);
        reportModel.setReportData(query.getResultList());

        return reportModel;
    }
}
