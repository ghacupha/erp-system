package io.github.erp.repository;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
