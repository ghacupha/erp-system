package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.LeaseLiabilityReportItem;
import io.github.erp.internal.model.LeaseLiabilityReportItemREPO;
import io.github.erp.service.dto.LeaseLiabilityReportItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Spring Data SQL repository for the LeaseLiabilityReportItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalLeaseLiabilityReportItemRepository
    extends JpaRepository<LeaseLiabilityReportItem, Long>, JpaSpecificationExecutor<LeaseLiabilityReportItem> {

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT " +
            "   ll.id as id, " +
            "   ll.lease_id as bookingId, " +
            "   lc.short_title as leaseTitle," +
            "   'Liability a/c' as liabilityAccountNumber," +
            "   sched.outstanding_balance as liabilityAmount," +
            "   'Interest Payable a/c' as interestPayableAccountNumber," +
            "   sched.interest_payable_closing as interestPayableAmount " +
            "FROM lease_liability_schedule_item sched " +
            "LEFT JOIN lease_liability ll ON sched.lease_liability_id = ll.id " +
            "LEFT JOIN ifrs16lease_contract lc ON sched.lease_contract_id = lc.id " +
            "LEFT JOIN lease_repayment_period lp ON sched.lease_period_id = lp.id " +
            "WHERE sched.lease_period_id = :leasePeriodId",
        countQuery = "" +
            "SELECT " +
            "   ll.id as id, " +
            "   ll.lease_id as bookingId, " +
            "   lc.short_title as leaseTitle," +
            "   'Liability a/c' as liabilityAccountNumber," +
            "   sched.outstanding_balance as liabilityAmount," +
            "   'Interest Payable a/c' as interestPayableAccountNumber," +
            "   sched.interest_payable_closing as interestPayableAmount " +
            "FROM lease_liability_schedule_item sched " +
            "LEFT JOIN lease_liability ll ON sched.lease_liability_id = ll.id " +
            "LEFT JOIN ifrs16lease_contract lc ON sched.lease_contract_id = lc.id " +
            "LEFT JOIN lease_repayment_period lp ON sched.lease_period_id = lp.id " +
            "WHERE sched.lease_period_id = :leasePeriodId"
    )
    Page<LeaseLiabilityReportItemREPO> leaseLiabilityReportItemsByLeasePeriod(@Param("leasePeriodId") Long leasePeriodId, Pageable pageable);
}
