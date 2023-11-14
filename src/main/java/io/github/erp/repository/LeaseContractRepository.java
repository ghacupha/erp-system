package io.github.erp.repository;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import io.github.erp.domain.LeaseContract;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LeaseContract entity.
 */
@Repository
public interface LeaseContractRepository extends JpaRepository<LeaseContract, Long>, JpaSpecificationExecutor<LeaseContract> {
    @Query(
        value = "select distinct leaseContract from LeaseContract leaseContract left join fetch leaseContract.placeholders left join fetch leaseContract.systemMappings left join fetch leaseContract.businessDocuments left join fetch leaseContract.contractMetadata",
        countQuery = "select count(distinct leaseContract) from LeaseContract leaseContract"
    )
    Page<LeaseContract> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct leaseContract from LeaseContract leaseContract left join fetch leaseContract.placeholders left join fetch leaseContract.systemMappings left join fetch leaseContract.businessDocuments left join fetch leaseContract.contractMetadata"
    )
    List<LeaseContract> findAllWithEagerRelationships();

    @Query(
        "select leaseContract from LeaseContract leaseContract left join fetch leaseContract.placeholders left join fetch leaseContract.systemMappings left join fetch leaseContract.businessDocuments left join fetch leaseContract.contractMetadata where leaseContract.id =:id"
    )
    Optional<LeaseContract> findOneWithEagerRelationships(@Param("id") Long id);
}
