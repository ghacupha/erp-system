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
import io.github.erp.domain.PurchaseOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PurchaseOrder entity.
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {
    @Query(
        value = "select distinct purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.placeholders left join fetch purchaseOrder.signatories left join fetch purchaseOrder.businessDocuments",
        countQuery = "select count(distinct purchaseOrder) from PurchaseOrder purchaseOrder"
    )
    Page<PurchaseOrder> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.placeholders left join fetch purchaseOrder.signatories left join fetch purchaseOrder.businessDocuments"
    )
    List<PurchaseOrder> findAllWithEagerRelationships();

    @Query(
        "select purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.placeholders left join fetch purchaseOrder.signatories left join fetch purchaseOrder.businessDocuments where purchaseOrder.id =:id"
    )
    Optional<PurchaseOrder> findOneWithEagerRelationships(@Param("id") Long id);
}
