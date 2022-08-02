package io.github.erp.repository;

import io.github.erp.domain.FixedAssetDepreciation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FixedAssetDepreciation entity.
 */
@Repository
public interface FixedAssetDepreciationRepository
    extends JpaRepository<FixedAssetDepreciation, Long>, JpaSpecificationExecutor<FixedAssetDepreciation> {
    @Query(
        value = "select distinct fixedAssetDepreciation from FixedAssetDepreciation fixedAssetDepreciation left join fetch fixedAssetDepreciation.placeholders",
        countQuery = "select count(distinct fixedAssetDepreciation) from FixedAssetDepreciation fixedAssetDepreciation"
    )
    Page<FixedAssetDepreciation> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct fixedAssetDepreciation from FixedAssetDepreciation fixedAssetDepreciation left join fetch fixedAssetDepreciation.placeholders"
    )
    List<FixedAssetDepreciation> findAllWithEagerRelationships();

    @Query(
        "select fixedAssetDepreciation from FixedAssetDepreciation fixedAssetDepreciation left join fetch fixedAssetDepreciation.placeholders where fixedAssetDepreciation.id =:id"
    )
    Optional<FixedAssetDepreciation> findOneWithEagerRelationships(@Param("id") Long id);
}
