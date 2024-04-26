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
        value = "select distinct assetRegistration from AssetRegistration assetRegistration left join fetch assetRegistration.placeholders left join fetch assetRegistration.paymentInvoices left join fetch assetRegistration.otherRelatedServiceOutlets left join fetch assetRegistration.otherRelatedSettlements left join fetch assetRegistration.purchaseOrders left join fetch assetRegistration.deliveryNotes left join fetch assetRegistration.jobSheets left join fetch assetRegistration.designatedUsers left join fetch assetRegistration.businessDocuments left join fetch assetRegistration.assetWarranties left join fetch assetRegistration.universallyUniqueMappings left join fetch assetRegistration.assetAccessories",
        countQuery = "select count(distinct assetRegistration) from AssetRegistration assetRegistration"
    )
    Page<AssetRegistration> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct assetRegistration from AssetRegistration assetRegistration left join fetch assetRegistration.placeholders left join fetch assetRegistration.paymentInvoices left join fetch assetRegistration.otherRelatedServiceOutlets left join fetch assetRegistration.otherRelatedSettlements left join fetch assetRegistration.purchaseOrders left join fetch assetRegistration.deliveryNotes left join fetch assetRegistration.jobSheets left join fetch assetRegistration.designatedUsers left join fetch assetRegistration.businessDocuments left join fetch assetRegistration.assetWarranties left join fetch assetRegistration.universallyUniqueMappings left join fetch assetRegistration.assetAccessories"
    )
    List<AssetRegistration> findAllWithEagerRelationships();

    @Query(
        "select assetRegistration from AssetRegistration assetRegistration left join fetch assetRegistration.placeholders left join fetch assetRegistration.paymentInvoices left join fetch assetRegistration.otherRelatedServiceOutlets left join fetch assetRegistration.otherRelatedSettlements left join fetch assetRegistration.purchaseOrders left join fetch assetRegistration.deliveryNotes left join fetch assetRegistration.jobSheets left join fetch assetRegistration.designatedUsers left join fetch assetRegistration.businessDocuments left join fetch assetRegistration.assetWarranties left join fetch assetRegistration.universallyUniqueMappings left join fetch assetRegistration.assetAccessories where assetRegistration.id =:id"
    )
    Optional<AssetRegistration> findOneWithEagerRelationships(@Param("id") Long id);
}
