package io.github.erp.repository;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

import io.github.erp.domain.AmortizationSequence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AmortizationSequence entity.
 */
@Repository
public interface AmortizationSequenceRepository
    extends JpaRepository<AmortizationSequence, Long>, JpaSpecificationExecutor<AmortizationSequence> {
    @Query(
        value = "select distinct amortizationSequence from AmortizationSequence amortizationSequence left join fetch amortizationSequence.placeholders left join fetch amortizationSequence.prepaymentMappings left join fetch amortizationSequence.applicationParameters",
        countQuery = "select count(distinct amortizationSequence) from AmortizationSequence amortizationSequence"
    )
    Page<AmortizationSequence> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct amortizationSequence from AmortizationSequence amortizationSequence left join fetch amortizationSequence.placeholders left join fetch amortizationSequence.prepaymentMappings left join fetch amortizationSequence.applicationParameters"
    )
    List<AmortizationSequence> findAllWithEagerRelationships();

    @Query(
        "select amortizationSequence from AmortizationSequence amortizationSequence left join fetch amortizationSequence.placeholders left join fetch amortizationSequence.prepaymentMappings left join fetch amortizationSequence.applicationParameters where amortizationSequence.id =:id"
    )
    Optional<AmortizationSequence> findOneWithEagerRelationships(@Param("id") Long id);
}
