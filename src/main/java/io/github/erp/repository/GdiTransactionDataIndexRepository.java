package io.github.erp.repository;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.GdiTransactionDataIndex;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GdiTransactionDataIndex entity.
 */
@Repository
public interface GdiTransactionDataIndexRepository
    extends JpaRepository<GdiTransactionDataIndex, Long>, JpaSpecificationExecutor<GdiTransactionDataIndex> {
    @Query(
        value = "select distinct gdiTransactionDataIndex from GdiTransactionDataIndex gdiTransactionDataIndex left join fetch gdiTransactionDataIndex.masterDataItems left join fetch gdiTransactionDataIndex.placeholders",
        countQuery = "select count(distinct gdiTransactionDataIndex) from GdiTransactionDataIndex gdiTransactionDataIndex"
    )
    Page<GdiTransactionDataIndex> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct gdiTransactionDataIndex from GdiTransactionDataIndex gdiTransactionDataIndex left join fetch gdiTransactionDataIndex.masterDataItems left join fetch gdiTransactionDataIndex.placeholders"
    )
    List<GdiTransactionDataIndex> findAllWithEagerRelationships();

    @Query(
        "select gdiTransactionDataIndex from GdiTransactionDataIndex gdiTransactionDataIndex left join fetch gdiTransactionDataIndex.masterDataItems left join fetch gdiTransactionDataIndex.placeholders where gdiTransactionDataIndex.id =:id"
    )
    Optional<GdiTransactionDataIndex> findOneWithEagerRelationships(@Param("id") Long id);
}
