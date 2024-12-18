package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
    List<AssetRegistration> findAllByCapitalizationDateLessThanEqual(LocalDate capitalizationDate);

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
        value = "SELECT CAST(id AS BIGINT) FROM public.asset_registration",
        countQuery = "SELECT id FROM public.asset_registration"
    )
    List<Long> findAllIds();

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT " +
            "   CAST(reg.id  AS BIGINT) " +
            "  FROM public.asset_registration reg " +
            "  LEFT JOIN asset_general_adjustment aga on reg.id = aga.asset_registration_id " +
            "  WHERE aga.asset_registration_id IS NOT NULL",
        countQuery = "" +
            "SELECT " +
            "   CAST(reg.id  AS BIGINT) " +
            "  FROM public.asset_registration reg " +
            "  LEFT JOIN asset_general_adjustment aga on reg.id = aga.asset_registration_id " +
            "  WHERE aga.asset_registration_id IS NOT NULL"
    )
    List<Long> findAdjacentAssetIds();

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT " +
            "   CAST(reg.id  AS BIGINT) " +
            "  FROM public.asset_registration reg " +
            "  LEFT JOIN asset_disposal dis on reg.id = dis.asset_disposed_id " +
            "  WHERE dis.asset_disposed_id IS NOT NULL",
        countQuery = "" +
            "SELECT " +
            "   CAST(reg.id  AS BIGINT) " +
            "  FROM public.asset_registration reg " +
            "  LEFT JOIN asset_disposal dis on reg.id = dis.asset_disposed_id " +
            "  WHERE dis.asset_disposed_id IS NOT NULL"
    )
    List<Long> findDisposedAssetIds();

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT " +
            "   CAST(reg.id  AS BIGINT) " +
            " FROM public.asset_registration reg " +
            " LEFT JOIN asset_write_off wr on reg.id = wr.asset_written_off_id " +
            " WHERE wr.asset_written_off_id IS NOT NULL",
        countQuery = "" +
            "SELECT " +
            "   CAST(reg.id  AS BIGINT) " +
            " FROM public.asset_registration reg " +
            " LEFT JOIN asset_write_off wr on reg.id = wr.asset_written_off_id " +
            " WHERE wr.asset_written_off_id IS NOT NULL"
    )
    List<Long> findWrittenOffAssetIds();
}
