package io.github.erp.repository;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.6
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

import io.github.erp.domain.WorkInProgressRegistration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkInProgressRegistration entity.
 */
@Repository
public interface WorkInProgressRegistrationRepository
    extends JpaRepository<WorkInProgressRegistration, Long>, JpaSpecificationExecutor<WorkInProgressRegistration> {
    @Query(
        value = "select distinct workInProgressRegistration from WorkInProgressRegistration workInProgressRegistration left join fetch workInProgressRegistration.placeholders left join fetch workInProgressRegistration.paymentInvoices left join fetch workInProgressRegistration.serviceOutlets left join fetch workInProgressRegistration.settlements left join fetch workInProgressRegistration.purchaseOrders left join fetch workInProgressRegistration.deliveryNotes left join fetch workInProgressRegistration.jobSheets left join fetch workInProgressRegistration.businessDocuments left join fetch workInProgressRegistration.assetAccessories left join fetch workInProgressRegistration.assetWarranties",
        countQuery = "select count(distinct workInProgressRegistration) from WorkInProgressRegistration workInProgressRegistration"
    )
    Page<WorkInProgressRegistration> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct workInProgressRegistration from WorkInProgressRegistration workInProgressRegistration left join fetch workInProgressRegistration.placeholders left join fetch workInProgressRegistration.paymentInvoices left join fetch workInProgressRegistration.serviceOutlets left join fetch workInProgressRegistration.settlements left join fetch workInProgressRegistration.purchaseOrders left join fetch workInProgressRegistration.deliveryNotes left join fetch workInProgressRegistration.jobSheets left join fetch workInProgressRegistration.businessDocuments left join fetch workInProgressRegistration.assetAccessories left join fetch workInProgressRegistration.assetWarranties"
    )
    List<WorkInProgressRegistration> findAllWithEagerRelationships();

    @Query(
        "select workInProgressRegistration from WorkInProgressRegistration workInProgressRegistration left join fetch workInProgressRegistration.placeholders left join fetch workInProgressRegistration.paymentInvoices left join fetch workInProgressRegistration.serviceOutlets left join fetch workInProgressRegistration.settlements left join fetch workInProgressRegistration.purchaseOrders left join fetch workInProgressRegistration.deliveryNotes left join fetch workInProgressRegistration.jobSheets left join fetch workInProgressRegistration.businessDocuments left join fetch workInProgressRegistration.assetAccessories left join fetch workInProgressRegistration.assetWarranties where workInProgressRegistration.id =:id"
    )
    Optional<WorkInProgressRegistration> findOneWithEagerRelationships(@Param("id") Long id);
}
