package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.LeasePeriod;
import io.github.erp.domain.LeaseRepaymentPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the LeaseRepaymentPeriod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalLeaseRepaymentPeriodRepository
    extends JpaRepository<LeaseRepaymentPeriod, Long>, JpaSpecificationExecutor<LeaseRepaymentPeriod> {

    @Query(
        nativeQuery = true,
        value = "" +
            "WITH RECURSIVE LeasePeriods AS ( " +
            "    SELECT *, " +
            "           1 AS iteration " +
            "    FROM lease_repayment_period " +
            "    WHERE start_date <= :commencementDate " +
            "      AND end_date >= :commencementDate " +
            " " +
            "    UNION ALL " +
            " " +
            "    SELECT lp.*, " +
            "           prev.iteration + 1 AS iteration " +
            "    FROM LeasePeriods prev " +
            "             JOIN lease_repayment_period lp ON lp.sequence_number = prev.sequence_number + 1 " +
            "    WHERE prev.end_date < ( " +
            "        SELECT MAX(end_date) FROM lease_repayment_period " + // -- This computes the end date condition so that the query never exceeds the periods available currently on the leaseTermPeriods table
            "    ) " +
            "      AND prev.iteration < :leaseTermPeriods " + //  -- Limit the number of iterations to the leaseTerm periods
            ") " +
            "SELECT * FROM LeasePeriods;",
        countQuery = "" +
            "WITH RECURSIVE LeasePeriods AS ( " +
            "    SELECT *, " +
            "           1 AS iteration " +
            "    FROM lease_repayment_period " +
            "    WHERE start_date <= :commencementDate " +
            "      AND end_date >= :commencementDate " +
            " " +
            "    UNION ALL " +
            " " +
            "    SELECT lp.*, " +
            "           prev.iteration + 1 AS iteration " +
            "    FROM LeasePeriods prev " +
            "             JOIN lease_repayment_period lp ON lp.sequence_number = prev.sequence_number + 1 " +
            "    WHERE prev.end_date < ( " +
            "        SELECT MAX(end_date) FROM lease_repayment_period " + // -- This computes the end date condition so that the query never exceeds the periods available currently on the leaseTermPeriods table
            "    ) " +
            "      AND prev.iteration < :leaseTermPeriods " + //  -- Limit the number of iterations to the leaseTerm periods
            ") " +
            "SELECT * FROM LeasePeriods;"
    )
    Optional<List<LeaseRepaymentPeriod>> findLeasePeriods(@Param("commencementDate") LocalDate commencementDate, @Param("leaseTermPeriods") int leaseTermPeriods);

}
