package io.github.erp.repository;

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

import io.github.erp.domain.JobSheet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the JobSheet entity.
 */
@Repository
public interface JobSheetRepository extends JpaRepository<JobSheet, Long>, JpaSpecificationExecutor<JobSheet> {
    @Query(
        value = "select distinct jobSheet from JobSheet jobSheet left join fetch jobSheet.signatories left join fetch jobSheet.businessStamps left join fetch jobSheet.placeholders left join fetch jobSheet.paymentLabels left join fetch jobSheet.businessDocuments",
        countQuery = "select count(distinct jobSheet) from JobSheet jobSheet"
    )
    Page<JobSheet> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct jobSheet from JobSheet jobSheet left join fetch jobSheet.signatories left join fetch jobSheet.businessStamps left join fetch jobSheet.placeholders left join fetch jobSheet.paymentLabels left join fetch jobSheet.businessDocuments"
    )
    List<JobSheet> findAllWithEagerRelationships();

    @Query(
        "select jobSheet from JobSheet jobSheet left join fetch jobSheet.signatories left join fetch jobSheet.businessStamps left join fetch jobSheet.placeholders left join fetch jobSheet.paymentLabels left join fetch jobSheet.businessDocuments where jobSheet.id =:id"
    )
    Optional<JobSheet> findOneWithEagerRelationships(@Param("id") Long id);
}
