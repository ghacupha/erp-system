package io.github.erp.repository;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.0.8-SNAPSHOT
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
import io.github.erp.domain.ReportRequisition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReportRequisition entity.
 */
@Repository
public interface ReportRequisitionRepository extends JpaRepository<ReportRequisition, Long>, JpaSpecificationExecutor<ReportRequisition> {
    @Query(
        value = "select distinct reportRequisition from ReportRequisition reportRequisition left join fetch reportRequisition.placeholders left join fetch reportRequisition.parameters",
        countQuery = "select count(distinct reportRequisition) from ReportRequisition reportRequisition"
    )
    Page<ReportRequisition> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct reportRequisition from ReportRequisition reportRequisition left join fetch reportRequisition.placeholders left join fetch reportRequisition.parameters"
    )
    List<ReportRequisition> findAllWithEagerRelationships();

    @Query(
        "select reportRequisition from ReportRequisition reportRequisition left join fetch reportRequisition.placeholders left join fetch reportRequisition.parameters where reportRequisition.id =:id"
    )
    Optional<ReportRequisition> findOneWithEagerRelationships(@Param("id") Long id);
}
