package io.github.erp.internal.repository;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    @Query(value = "SELECT " +
        "COALESCE(COUNT(DISTINCT w.sequence_number), 0) AS numberOfItems, " +
        "COALESCE(SUM(wir.instalment_amount), 0.0) AS instalmentAmount, " +
        "COALESCE(SUM(ta.transfer_amount), 0.0) AS totalTransferAmount, " +
        "(COALESCE(SUM(wir.instalment_amount), 0.0) - COALESCE(SUM(ta.transfer_amount), 0.0)) AS outstandingAmount " +
        "FROM work_in_progress_registration w " +
        "LEFT JOIN Settlement s ON w.settlement_transaction_id = s.id " +
        "LEFT JOIN (SELECT work_in_progress_registration_id, SUM(transfer_amount) AS transfer_amount FROM work_in_progress_transfer GROUP BY work_in_progress_registration_id) ta ON ta.work_in_progress_registration_id = w.id " +
        "LEFT JOIN work_in_progress_registration wir ON wir.id = w.id " +
        "WHERE s.payment_date <= :reportDate", nativeQuery = true)
    Optional<WorkInProgressOverviewTuple> findByReportDate(@Param("reportDate") LocalDate reportDate);

}
