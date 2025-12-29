package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.RouInitialDirectCost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;        
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the RouInitialDirectCost entity.
 */
@Repository
public interface InternalRouInitialDirectCostRepository
    extends JpaRepository<RouInitialDirectCost, Long>, JpaSpecificationExecutor<RouInitialDirectCost> {
    @Query(
        value = "select distinct rouInitialDirectCost from RouInitialDirectCost rouInitialDirectCost left join fetch rouInitialDirectCost.placeholders",
        countQuery = "select count(distinct rouInitialDirectCost) from RouInitialDirectCost rouInitialDirectCost"
    )
    Page<RouInitialDirectCost> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct rouInitialDirectCost from RouInitialDirectCost rouInitialDirectCost left join fetch rouInitialDirectCost.placeholders"
    )
    List<RouInitialDirectCost> findAllWithEagerRelationships();

    @Query(
        "select rouInitialDirectCost from RouInitialDirectCost rouInitialDirectCost left join fetch rouInitialDirectCost.placeholders where rouInitialDirectCost.id =:id"
    )
    Optional<RouInitialDirectCost> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(
        nativeQuery = true,
        value = "SELECT CAST(reference_number AS BIGINT) FROM public.rou_initial_direct_cost",
        countQuery = "SELECT CAST(reference_number AS BIGINT) FROM public.rou_initial_direct_cost"
    )
    List<Long> findAllReferenceNumbers();

    @Query("select coalesce(sum(rouInitialDirectCost.cost), 0) from RouInitialDirectCost rouInitialDirectCost where rouInitialDirectCost.leaseContract.id = :leaseContractId")
    BigDecimal sumCostByLeaseContractId(@Param("leaseContractId") Long leaseContractId);
}
