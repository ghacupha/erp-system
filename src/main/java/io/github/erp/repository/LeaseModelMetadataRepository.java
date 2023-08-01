package io.github.erp.repository;

import io.github.erp.domain.LeaseModelMetadata;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LeaseModelMetadata entity.
 */
@Repository
public interface LeaseModelMetadataRepository
    extends JpaRepository<LeaseModelMetadata, Long>, JpaSpecificationExecutor<LeaseModelMetadata> {
    @Query(
        value = "select distinct leaseModelMetadata from LeaseModelMetadata leaseModelMetadata left join fetch leaseModelMetadata.placeholders left join fetch leaseModelMetadata.leaseMappings",
        countQuery = "select count(distinct leaseModelMetadata) from LeaseModelMetadata leaseModelMetadata"
    )
    Page<LeaseModelMetadata> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct leaseModelMetadata from LeaseModelMetadata leaseModelMetadata left join fetch leaseModelMetadata.placeholders left join fetch leaseModelMetadata.leaseMappings"
    )
    List<LeaseModelMetadata> findAllWithEagerRelationships();

    @Query(
        "select leaseModelMetadata from LeaseModelMetadata leaseModelMetadata left join fetch leaseModelMetadata.placeholders left join fetch leaseModelMetadata.leaseMappings where leaseModelMetadata.id =:id"
    )
    Optional<LeaseModelMetadata> findOneWithEagerRelationships(@Param("id") Long id);
}
