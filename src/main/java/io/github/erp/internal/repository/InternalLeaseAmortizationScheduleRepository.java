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

import io.github.erp.domain.LeaseAmortizationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.config.web.servlet.PortMapperDsl;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the LeaseAmortizationSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalLeaseAmortizationScheduleRepository
    extends JpaRepository<LeaseAmortizationSchedule, Long>, JpaSpecificationExecutor<LeaseAmortizationSchedule> {

    /**
     * Extract adjacent schedule based on the bookingId provided
     *
     * @param bookingId value of the IFRS16-lease-contract
     * @return Schedule instance
     */
    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT lam.id, identifier, lease_liability_id, lease_contract_id " +
            " FROM public.lease_amortization_schedule lam " +
            " LEFT JOIN ifrs16lease_contract contract on contract.id = lam.lease_contract_id " +
            " WHERE contract.booking_id = :bookingId"
    )
    Optional<LeaseAmortizationSchedule> findAdjacentScheduleByBookingId(@Param("bookingId") String bookingId);
}
