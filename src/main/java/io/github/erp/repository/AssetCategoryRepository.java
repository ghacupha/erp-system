package io.github.erp.repository;

import io.github.erp.domain.AssetCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetCategory entity.
 */
@Repository
public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Long>, JpaSpecificationExecutor<AssetCategory> {
    @Query(
        value = "select distinct assetCategory from AssetCategory assetCategory left join fetch assetCategory.placeholders",
        countQuery = "select count(distinct assetCategory) from AssetCategory assetCategory"
    )
    Page<AssetCategory> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct assetCategory from AssetCategory assetCategory left join fetch assetCategory.placeholders")
    List<AssetCategory> findAllWithEagerRelationships();

    @Query("select assetCategory from AssetCategory assetCategory left join fetch assetCategory.placeholders where assetCategory.id =:id")
    Optional<AssetCategory> findOneWithEagerRelationships(@Param("id") Long id);
}
