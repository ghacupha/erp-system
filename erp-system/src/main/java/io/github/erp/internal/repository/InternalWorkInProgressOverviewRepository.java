package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.WorkInProgressOutstandingReportREPO;
import io.github.erp.domain.WorkInProgressOverview;
import io.github.erp.domain.WorkInProgressOverviewDTO;
import io.github.erp.domain.WorkInProgressOverviewTuple;
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

    @Query(value = "" +
        "SELECT " +
        " COALESCE(COUNT(DISTINCT w.sequence_number), 0) AS numberOfItems,  " +
        " COALESCE(SUM(w.instalment_amount), 0.0) AS instalmentAmount,  " +
        " COALESCE(SUM(ta.transfer_amount), 0.0) AS totalTransferAmount,  " +
        " (COALESCE(SUM(w.instalment_amount), 0.0) - COALESCE(SUM(ta.transfer_amount), 0.0)) AS outstandingAmount  " +
        "FROM work_in_progress_registration w " +
        " LEFT JOIN (" +
        "    SELECT tra.work_in_progress_registration_id, " +
        "       SUM(tra.transfer_amount) AS transfer_amount " +
        "    FROM work_in_progress_transfer tra " +
        "    WHERE tra.transfer_date <= :reportDate " +
        "    GROUP BY work_in_progress_registration_id) ta " +
        "  ON ta.work_in_progress_registration_id = w.id  " +
        "WHERE w.instalment_date <= :reportDate", nativeQuery = true)
    Optional<WorkInProgressOverviewTuple> findByReportDate(@Param("reportDate") LocalDate reportDate);

}
