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
import io.github.erp.domain.ReportStatus;
import io.github.erp.repository.ReportStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InternalReportStatusRepository extends ReportStatusRepository, JpaRepository<ReportStatus, Long>, JpaSpecificationExecutor<ReportStatus> {

    @Query("select reportStatus " +
        "from ReportStatus reportStatus " +
        "left join fetch reportStatus.placeholders " +
        "where reportStatus.reportId =:reportId")
    Optional<ReportStatus> findReportStatusByReportIdEquals(@Param("reportId") UUID reportId);
}
