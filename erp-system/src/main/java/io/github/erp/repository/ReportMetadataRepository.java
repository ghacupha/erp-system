package io.github.erp.repository;

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

import io.github.erp.domain.ReportMetadata;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the {@link ReportMetadata} entity.
 */
@Repository
public interface ReportMetadataRepository extends JpaRepository<ReportMetadata, Long>, JpaSpecificationExecutor<ReportMetadata> {
    Optional<ReportMetadata> findOneByPagePath(String pagePath);

    @Query(
        "select rm from ReportMetadata rm where rm.active = true and (lower(rm.reportTitle) like lower(concat('%', :term, '%')) " +
        "or lower(rm.description) like lower(concat('%', :term, '%')) or lower(rm.module) like lower(concat('%', :term, '%')))"
    )
    Page<ReportMetadata> searchActiveByTerm(@Param("term") String term, Pageable pageable);
}
