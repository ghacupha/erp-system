package io.github.erp.repository.reports;

import io.github.erp.domain.SettlementRequisition;
import io.github.erp.erp.reports.SettlementBillerReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface SettlementRequisitionReportsRepository extends JpaRepository<SettlementRequisition, Long>, JpaSpecificationExecutor<SettlementRequisition> {

    @Query(
        "SELECT\n" +
            "       r.id,\n" +
            "       r.description,\n" +
            "       r.timeOfRequisition,\n" +
            "       r.requisitionNumber,\n" +
            "       s.iso4217CurrencyCode,\n" +
            "       r.paymentAmount,\n" +
            "       r.paymentStatus,\n" +
            "       d.dealerName as biller,\n" +
            "       a.applicationIdentity as current_owner,\n" +
            "       n.applicationIdentity as native_owner,\n" +
            "       nd.dealerName as native_department\n" +
            "FROM SettlementRequisition r,\n" +
            "     SettlementCurrency s,\n" +
            "     Dealer d,\n" +
            "     ApplicationUser a,\n" +
            "     ApplicationUser n,\n" +
            "     Dealer nd\n" +
            "WHERE r.settlementCurrency.id = s.id\n" +
            "  AND r.nativeDepartment.id = nd.id\n" +
            "  AND r.nativeOwner.id = n.id\n" +
            "  AND r.currentOwner.id = a.id\n" +
            "  AND r.biller.id = d.id\n" +
            "  AND r.timeOfRequisition >= :fromTime\n" +
            "  AND r.timeOfRequisition < :toTime\n" +
            "  AND r.biller.id = :billerId"
    )
    List<SettlementBillerReportView> findAllByBillerIdAndTimeOfRequisition(@Param("billerId") long billerId, @Param("fromTime") ZonedDateTime fromTime, @Param("toTime") ZonedDateTime toTime);
}
