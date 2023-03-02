package io.github.erp.repository.reports;

/*-
 * Erp System - Mark III No 11 (Caleb Series) Server ver 0.7.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
