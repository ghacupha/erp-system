package io.github.erp.internal.repository;

import io.github.erp.domain.AssetRegistration;
import io.github.erp.repository.AssetRegistrationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternalAssetRegistrationRepository
    extends AssetRegistrationRepository,
    JpaRepository<AssetRegistration, Long>, JpaSpecificationExecutor<AssetRegistration> {

    @Query(
        value = "select distinct assetRegistration " +
            "from AssetRegistration assetRegistration " +
            "left join fetch assetRegistration.placeholders " +
            "left join fetch assetRegistration.paymentInvoices " +
            "left join fetch assetRegistration.otherRelatedServiceOutlets " +
            "left join fetch assetRegistration.otherRelatedSettlements " +
            "left join fetch assetRegistration.purchaseOrders " +
            "left join fetch assetRegistration.deliveryNotes " +
            "left join fetch assetRegistration.jobSheets " +
            "left join fetch assetRegistration.designatedUsers " +
            "left join fetch assetRegistration.businessDocuments " +
            "left join fetch assetRegistration.assetWarranties " +
            "left join fetch assetRegistration.universallyUniqueMappings " +
            "left join fetch assetRegistration.assetAccessories",
        countQuery = "select count(distinct assetRegistration) from AssetRegistration assetRegistration"
    )
    Page<AssetRegistration> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct assetRegistration " +
            "from AssetRegistration assetRegistration " +
            "left join fetch assetRegistration.placeholders " +
            "left join fetch assetRegistration.paymentInvoices " +
            "left join fetch assetRegistration.otherRelatedServiceOutlets " +
            "left join fetch assetRegistration.otherRelatedSettlements " +
            "left join fetch assetRegistration.purchaseOrders " +
            "left join fetch assetRegistration.deliveryNotes " +
            "left join fetch assetRegistration.jobSheets " +
            "left join fetch assetRegistration.designatedUsers " +
            "left join fetch assetRegistration.businessDocuments " +
            "left join fetch assetRegistration.assetWarranties " +
            "left join fetch assetRegistration.universallyUniqueMappings " +
            "left join fetch assetRegistration.assetAccessories"
    )
    List<AssetRegistration> findAllWithEagerRelationships();

    @Query(
        "select assetRegistration " +
            "from AssetRegistration assetRegistration " +
            "left join fetch assetRegistration.acquiringTransaction " +
            "left join fetch assetRegistration.placeholders " +
            "left join fetch assetRegistration.paymentInvoices " +
            "left join fetch assetRegistration.otherRelatedServiceOutlets " +
            "left join fetch assetRegistration.otherRelatedSettlements " +
            "left join fetch assetRegistration.purchaseOrders " +
            "left join fetch assetRegistration.deliveryNotes " +
            "left join fetch assetRegistration.jobSheets " +
            "left join fetch assetRegistration.designatedUsers " +
            "left join fetch assetRegistration.businessDocuments " +
            "left join fetch assetRegistration.assetWarranties " +
            "left join fetch assetRegistration.universallyUniqueMappings " +
            "left join fetch assetRegistration.assetAccessories " +
            "where assetRegistration.id =:id"
    )
    Optional<AssetRegistration> findOneWithEagerRelationships(@Param("id") Long id);
}
