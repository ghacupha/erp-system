package io.github.erp.repository;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
