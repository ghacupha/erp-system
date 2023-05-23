package io.github.erp.repository;

import io.github.erp.domain.AssetAccessory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetAccessory entity.
 */
@Repository
public interface AssetAccessoryRepository extends JpaRepository<AssetAccessory, Long>, JpaSpecificationExecutor<AssetAccessory> {
    @Query(
        value = "select distinct assetAccessory from AssetAccessory assetAccessory left join fetch assetAccessory.assetWarranties left join fetch assetAccessory.placeholders left join fetch assetAccessory.paymentInvoices left join fetch assetAccessory.serviceOutlets left join fetch assetAccessory.settlements left join fetch assetAccessory.purchaseOrders left join fetch assetAccessory.deliveryNotes left join fetch assetAccessory.jobSheets left join fetch assetAccessory.designatedUsers left join fetch assetAccessory.businessDocuments left join fetch assetAccessory.universallyUniqueMappings",
        countQuery = "select count(distinct assetAccessory) from AssetAccessory assetAccessory"
    )
    Page<AssetAccessory> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct assetAccessory from AssetAccessory assetAccessory left join fetch assetAccessory.assetWarranties left join fetch assetAccessory.placeholders left join fetch assetAccessory.paymentInvoices left join fetch assetAccessory.serviceOutlets left join fetch assetAccessory.settlements left join fetch assetAccessory.purchaseOrders left join fetch assetAccessory.deliveryNotes left join fetch assetAccessory.jobSheets left join fetch assetAccessory.designatedUsers left join fetch assetAccessory.businessDocuments left join fetch assetAccessory.universallyUniqueMappings"
    )
    List<AssetAccessory> findAllWithEagerRelationships();

    @Query(
        "select assetAccessory from AssetAccessory assetAccessory left join fetch assetAccessory.assetWarranties left join fetch assetAccessory.placeholders left join fetch assetAccessory.paymentInvoices left join fetch assetAccessory.serviceOutlets left join fetch assetAccessory.settlements left join fetch assetAccessory.purchaseOrders left join fetch assetAccessory.deliveryNotes left join fetch assetAccessory.jobSheets left join fetch assetAccessory.designatedUsers left join fetch assetAccessory.businessDocuments left join fetch assetAccessory.universallyUniqueMappings where assetAccessory.id =:id"
    )
    Optional<AssetAccessory> findOneWithEagerRelationships(@Param("id") Long id);
}
