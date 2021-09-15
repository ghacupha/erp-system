package io.github.erp.repository;

import io.github.erp.domain.FixedAssetNetBookValue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FixedAssetNetBookValue entity.
 */
@Repository
public interface FixedAssetNetBookValueRepository
    extends JpaRepository<FixedAssetNetBookValue, Long>, JpaSpecificationExecutor<FixedAssetNetBookValue> {
    @Query(
        value = "select distinct fixedAssetNetBookValue from FixedAssetNetBookValue fixedAssetNetBookValue left join fetch fixedAssetNetBookValue.placeholders",
        countQuery = "select count(distinct fixedAssetNetBookValue) from FixedAssetNetBookValue fixedAssetNetBookValue"
    )
    Page<FixedAssetNetBookValue> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct fixedAssetNetBookValue from FixedAssetNetBookValue fixedAssetNetBookValue left join fetch fixedAssetNetBookValue.placeholders"
    )
    List<FixedAssetNetBookValue> findAllWithEagerRelationships();

    @Query(
        "select fixedAssetNetBookValue from FixedAssetNetBookValue fixedAssetNetBookValue left join fetch fixedAssetNetBookValue.placeholders where fixedAssetNetBookValue.id =:id"
    )
    Optional<FixedAssetNetBookValue> findOneWithEagerRelationships(@Param("id") Long id);
}
