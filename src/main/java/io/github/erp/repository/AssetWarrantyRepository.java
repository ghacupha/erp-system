package io.github.erp.repository;

import io.github.erp.domain.AssetWarranty;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetWarranty entity.
 */
@Repository
public interface AssetWarrantyRepository extends JpaRepository<AssetWarranty, Long>, JpaSpecificationExecutor<AssetWarranty> {
    @Query(
        value = "select distinct assetWarranty from AssetWarranty assetWarranty left join fetch assetWarranty.placeholders left join fetch assetWarranty.universallyUniqueMappings left join fetch assetWarranty.warrantyAttachments",
        countQuery = "select count(distinct assetWarranty) from AssetWarranty assetWarranty"
    )
    Page<AssetWarranty> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct assetWarranty from AssetWarranty assetWarranty left join fetch assetWarranty.placeholders left join fetch assetWarranty.universallyUniqueMappings left join fetch assetWarranty.warrantyAttachments"
    )
    List<AssetWarranty> findAllWithEagerRelationships();

    @Query(
        "select assetWarranty from AssetWarranty assetWarranty left join fetch assetWarranty.placeholders left join fetch assetWarranty.universallyUniqueMappings left join fetch assetWarranty.warrantyAttachments where assetWarranty.id =:id"
    )
    Optional<AssetWarranty> findOneWithEagerRelationships(@Param("id") Long id);
}
