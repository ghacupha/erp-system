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

import io.github.erp.domain.ReportContentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReportContentType entity.
 */
@Repository
public interface ReportContentTypeRepository extends JpaRepository<ReportContentType, Long>, JpaSpecificationExecutor<ReportContentType> {
    @Query(
        value = "select distinct reportContentType from ReportContentType reportContentType left join fetch reportContentType.placeholders",
        countQuery = "select count(distinct reportContentType) from ReportContentType reportContentType"
    )
    Page<ReportContentType> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct reportContentType from ReportContentType reportContentType left join fetch reportContentType.placeholders")
    List<ReportContentType> findAllWithEagerRelationships();

    @Query(
        "select reportContentType from ReportContentType reportContentType left join fetch reportContentType.placeholders where reportContentType.id =:id"
    )
    Optional<ReportContentType> findOneWithEagerRelationships(@Param("id") Long id);
}
