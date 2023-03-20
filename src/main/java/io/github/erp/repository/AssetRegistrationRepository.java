package io.github.erp.repository;

import io.github.erp.domain.AssetRegistration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetRegistration entity.
 */
@Repository
public interface AssetRegistrationRepository extends JpaRepository<AssetRegistration, Long>, JpaSpecificationExecutor<AssetRegistration> {
    @Query(
        value = "select distinct assetRegistration from AssetRegistration assetRegistration left join fetch assetRegistration.placeholders left join fetch assetRegistration.paymentInvoices left join fetch assetRegistration.serviceOutlets left join fetch assetRegistration.settlements left join fetch assetRegistration.purchaseOrders left join fetch assetRegistration.deliveryNotes left join fetch assetRegistration.jobSheets left join fetch assetRegistration.designatedUsers left join fetch assetRegistration.businessDocuments",
        countQuery = "select count(distinct assetRegistration) from AssetRegistration assetRegistration"
    )
    Page<AssetRegistration> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct assetRegistration from AssetRegistration assetRegistration left join fetch assetRegistration.placeholders left join fetch assetRegistration.paymentInvoices left join fetch assetRegistration.serviceOutlets left join fetch assetRegistration.settlements left join fetch assetRegistration.purchaseOrders left join fetch assetRegistration.deliveryNotes left join fetch assetRegistration.jobSheets left join fetch assetRegistration.designatedUsers left join fetch assetRegistration.businessDocuments"
    )
    List<AssetRegistration> findAllWithEagerRelationships();

    @Query(
        "select assetRegistration from AssetRegistration assetRegistration left join fetch assetRegistration.placeholders left join fetch assetRegistration.paymentInvoices left join fetch assetRegistration.serviceOutlets left join fetch assetRegistration.settlements left join fetch assetRegistration.purchaseOrders left join fetch assetRegistration.deliveryNotes left join fetch assetRegistration.jobSheets left join fetch assetRegistration.designatedUsers left join fetch assetRegistration.businessDocuments where assetRegistration.id =:id"
    )
    Optional<AssetRegistration> findOneWithEagerRelationships(@Param("id") Long id);
}
