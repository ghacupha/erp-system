package io.github.erp.repository;

import io.github.erp.domain.FixedAssetAcquisition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FixedAssetAcquisition entity.
 */
@Repository
public interface FixedAssetAcquisitionRepository
    extends JpaRepository<FixedAssetAcquisition, Long>, JpaSpecificationExecutor<FixedAssetAcquisition> {
    @Query(
        value = "select distinct fixedAssetAcquisition from FixedAssetAcquisition fixedAssetAcquisition left join fetch fixedAssetAcquisition.placeholders",
        countQuery = "select count(distinct fixedAssetAcquisition) from FixedAssetAcquisition fixedAssetAcquisition"
    )
    Page<FixedAssetAcquisition> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct fixedAssetAcquisition from FixedAssetAcquisition fixedAssetAcquisition left join fetch fixedAssetAcquisition.placeholders"
    )
    List<FixedAssetAcquisition> findAllWithEagerRelationships();

    @Query(
        "select fixedAssetAcquisition from FixedAssetAcquisition fixedAssetAcquisition left join fetch fixedAssetAcquisition.placeholders where fixedAssetAcquisition.id =:id"
    )
    Optional<FixedAssetAcquisition> findOneWithEagerRelationships(@Param("id") Long id);
}
