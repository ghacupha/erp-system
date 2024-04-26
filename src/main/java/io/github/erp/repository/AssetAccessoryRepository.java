package io.github.erp.repository;

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
