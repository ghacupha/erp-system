package io.github.erp.internal.repository;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import io.github.erp.domain.WorkInProgressOutstandingReportREPO;
import io.github.erp.domain.WorkInProgressOverview;
import io.github.erp.domain.WorkInProgressOverviewDTO;
import io.github.erp.repository.WorkInProgressOverviewRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface InternalWorkInProgressOverviewRepository extends
    WorkInProgressOverviewRepository,
    JpaRepository<WorkInProgressOverview, Long>
{
    @Query("SELECT NEW io.github.erp.domain.WorkInProgressOverviewDTO (" +
        "COALESCE(COUNT(w.id),0) AS numberOfItems, " +
        "CAST(COALESCE(SUM(w.instalmentAmount), 0.0) AS java.math.BigDecimal) AS instalmentAmount, " +
        "CAST(COALESCE(SUM(ta.transferAmount), 0.0) AS java.math.BigDecimal) AS totalTransferAmount, " +
        "CAST(COALESCE(SUM(w.instalmentAmount), 0.0) AS java.math.BigDecimal) - CAST(COALESCE(SUM(ta.transferAmount), 0.0) AS java.math.BigDecimal) ) " +
        "FROM WorkInProgressRegistration w " +
        "JOIN Settlement s ON s.id = w.settlementTransaction.id " +
        "LEFT JOIN WorkInProgressTransfer ta ON ta.workInProgressRegistration.id = w.id " +
        "WHERE s.paymentDate <= :reportDate " +
        "AND (ta.transferDate IS NULL OR ta.transferDate <= :reportDate)")
    Optional<WorkInProgressOverviewDTO> findByReportDate(@Param("reportDate") LocalDate reportDate);

}
