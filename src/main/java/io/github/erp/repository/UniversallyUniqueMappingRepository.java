package io.github.erp.repository;

import io.github.erp.domain.UniversallyUniqueMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UniversallyUniqueMapping entity.
 */
@Repository
public interface UniversallyUniqueMappingRepository
    extends JpaRepository<UniversallyUniqueMapping, Long>, JpaSpecificationExecutor<UniversallyUniqueMapping> {
    @Query(
        value = "select distinct universallyUniqueMapping from UniversallyUniqueMapping universallyUniqueMapping left join fetch universallyUniqueMapping.placeholders",
        countQuery = "select count(distinct universallyUniqueMapping) from UniversallyUniqueMapping universallyUniqueMapping"
    )
    Page<UniversallyUniqueMapping> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct universallyUniqueMapping from UniversallyUniqueMapping universallyUniqueMapping left join fetch universallyUniqueMapping.placeholders"
    )
    List<UniversallyUniqueMapping> findAllWithEagerRelationships();

    @Query(
        "select universallyUniqueMapping from UniversallyUniqueMapping universallyUniqueMapping left join fetch universallyUniqueMapping.placeholders where universallyUniqueMapping.id =:id"
    )
    Optional<UniversallyUniqueMapping> findOneWithEagerRelationships(@Param("id") Long id);
}
