package io.github.erp.repository;

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
