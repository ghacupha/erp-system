package io.github.erp.repository;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.ContractMetadata;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContractMetadata entity.
 */
@Repository
public interface ContractMetadataRepository extends JpaRepository<ContractMetadata, Long>, JpaSpecificationExecutor<ContractMetadata> {
    @Query(
        value = "select distinct contractMetadata from ContractMetadata contractMetadata left join fetch contractMetadata.relatedContracts left join fetch contractMetadata.signatories left join fetch contractMetadata.placeholders left join fetch contractMetadata.contractDocumentFiles left join fetch contractMetadata.contractMappings",
        countQuery = "select count(distinct contractMetadata) from ContractMetadata contractMetadata"
    )
    Page<ContractMetadata> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct contractMetadata from ContractMetadata contractMetadata left join fetch contractMetadata.relatedContracts left join fetch contractMetadata.signatories left join fetch contractMetadata.placeholders left join fetch contractMetadata.contractDocumentFiles left join fetch contractMetadata.contractMappings"
    )
    List<ContractMetadata> findAllWithEagerRelationships();

    @Query(
        "select contractMetadata from ContractMetadata contractMetadata left join fetch contractMetadata.relatedContracts left join fetch contractMetadata.signatories left join fetch contractMetadata.placeholders left join fetch contractMetadata.contractDocumentFiles left join fetch contractMetadata.contractMappings where contractMetadata.id =:id"
    )
    Optional<ContractMetadata> findOneWithEagerRelationships(@Param("id") Long id);
}
