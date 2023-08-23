package io.github.erp.repository;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
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
import io.github.erp.domain.FiscalQuarter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FiscalQuarter entity.
 */
@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, Long>, JpaSpecificationExecutor<FiscalQuarter> {
    @Query(
        value = "select distinct fiscalQuarter from FiscalQuarter fiscalQuarter left join fetch fiscalQuarter.placeholders left join fetch fiscalQuarter.universallyUniqueMappings",
        countQuery = "select count(distinct fiscalQuarter) from FiscalQuarter fiscalQuarter"
    )
    Page<FiscalQuarter> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct fiscalQuarter from FiscalQuarter fiscalQuarter left join fetch fiscalQuarter.placeholders left join fetch fiscalQuarter.universallyUniqueMappings"
    )
    List<FiscalQuarter> findAllWithEagerRelationships();

    @Query(
        "select fiscalQuarter from FiscalQuarter fiscalQuarter left join fetch fiscalQuarter.placeholders left join fetch fiscalQuarter.universallyUniqueMappings where fiscalQuarter.id =:id"
    )
    Optional<FiscalQuarter> findOneWithEagerRelationships(@Param("id") Long id);
}
