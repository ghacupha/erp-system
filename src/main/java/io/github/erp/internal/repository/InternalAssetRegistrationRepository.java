package io.github.erp.internal.repository;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.AssetRegistration;
import io.github.erp.repository.AssetRegistrationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    /**
     * Applied in the depreciation module to exclude items capitalised after the end-date
     * of the depreciation period in the depreciation-job
     *
     * @param capitalizationDate
     * @return
     */
    List<AssetRegistration> findAllByCapitalizationDateBefore(LocalDate capitalizationDate);

    @Query(nativeQuery = true,
        value = "select " +
            "  a.id " +
            "from asset_registration a " +
            "  where " +
            "  a.capitalization_date <= :endDate ",
        countQuery = "select count( a.id ) from asset_registration a where a.capitalization_date <= :endDate "
    )
    List<Long> getAssetIdsByCapitalizationDateBefore(@Param("endDate") LocalDate capitalizationDate);

    @Query(nativeQuery = true,
        value = "select " +
            "  coalesce (sum(a.asset_cost),0) " +
            "from asset_registration a" +
            "  where" +
            "  a.capitalization_date <= :capitalizationDate "
    )
    BigDecimal getInitialAssetCostByCapitalizationDateBefore(@Param("capitalizationDate") LocalDate capitalizationDate);

    @Query(
        nativeQuery = true,
        value = "SELECT CAST(asset_number AS BIGINT) FROM public.asset_registration",
        countQuery = "SELECT asset_number FROM public.asset_registration"
    )
    List<Long> findAllIds();
}
