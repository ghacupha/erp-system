package io.github.erp.service.reports;

import io.github.erp.IntegrationTest;
import io.github.erp.erp.reports.SettlementBillerReportDTO;
import io.github.erp.erp.reports.SettlementBillerReportRequisitionDTO;
import io.github.erp.internal.report.ReportModel;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// @RunWith(SpringRunner.class)
@IntegrationTest
public class SettlementBillerReportRequisitionServiceJPQLIT {

    private static final String RESULT_QUERY =
        "select new io.github.erp.erp.reports.SettlementBillerReportDTO(" +
            "generatedAlias0.id, " +
            "generatedAlias0.description, " +
            "generatedAlias0.timeOfRequisition, " +
            "generatedAlias0.requisitionNumber, " +
            "generatedAlias1.iso4217CurrencyCode, " +
            "generatedAlias0.paymentAmount, " +
            "generatedAlias0.paymentStatus, " +
            "generatedAlias2.dealerName, " +
            "generatedAlias3.applicationIdentity, " +
            "generatedAlias4.applicationIdentity, " +
            "generatedAlias5.dealerName" +
            ") " +
            "from SettlementRequisition as generatedAlias0, " +
            "SettlementCurrency as generatedAlias1, " +
            "Dealer as generatedAlias2, " +
            "Dealer as generatedAlias5, " +
            "ApplicationUser as generatedAlias3, " +
            "ApplicationUser as generatedAlias4 " +
            "where " +
            "( generatedAlias0.settlementCurrency=generatedAlias1.id ) and " +
            "( generatedAlias1.id=generatedAlias0.biller ) and " +
            "( generatedAlias0.currentOwner=generatedAlias0.id ) and " +
            "( generatedAlias0.nativeOwner=generatedAlias4.id ) and " +
            "( generatedAlias0.nativeDepartment=generatedAlias5.dealerName ) and " +
            "( generatedAlias0.biller=1494 )";

    @Autowired
    @Qualifier("settlementBillerReportRequisitionServiceJPQL")
    private SettlementBillerReportRequisitionService reportModel;

    @Test
    public void sessionFactoryIsInjected() {
        assertThat(reportModel).isNotNull();
    }

    @Test
    @Transactional
    public void generateReport() throws Exception {
        ReportModel<List<SettlementBillerReportDTO>> reportSummaries = reportModel.generateReport(new SettlementBillerReportRequisitionDTO(1494, ZonedDateTime.now(), ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()) ));

        assertThat(reportSummaries.getReportQuery()).isEqualTo(RESULT_QUERY);
    }

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void whenMultipleEntitiesAreListedWithJoin_ThenCreatesMultipleJoins() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<TestRequisition> query =
            entityManager.createQuery(
                "SELECT NEW io.github.erp.service.reports.TestRequisition( " +
                    "r.id, d.dealerName, " +
                    "c.iso4217CurrencyCode, " +
                    "r.paymentAmount, " +
                    "o.applicationIdentity " +
                    ") " +
                    "FROM SettlementRequisition r " +
                    "JOIN r.biller d " +
                    "JOIN r.currentOwner o " +
                    "JOIN r.settlementCurrency c ",
                TestRequisition.class);

        List<TestRequisition> resultList = query.getResultList();
    }
}
