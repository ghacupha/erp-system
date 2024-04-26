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
import io.github.erp.domain.SettlementRequisition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SettlementRequisition entity.
 */
@Repository
public interface SettlementRequisitionRepository
    extends JpaRepository<SettlementRequisition, Long>, JpaSpecificationExecutor<SettlementRequisition> {
    @Query(
        value = "select distinct settlementRequisition from SettlementRequisition settlementRequisition left join fetch settlementRequisition.paymentInvoices left join fetch settlementRequisition.deliveryNotes left join fetch settlementRequisition.jobSheets left join fetch settlementRequisition.signatures left join fetch settlementRequisition.businessDocuments left join fetch settlementRequisition.applicationMappings left join fetch settlementRequisition.placeholders left join fetch settlementRequisition.settlements",
        countQuery = "select count(distinct settlementRequisition) from SettlementRequisition settlementRequisition"
    )
    Page<SettlementRequisition> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct settlementRequisition from SettlementRequisition settlementRequisition left join fetch settlementRequisition.paymentInvoices left join fetch settlementRequisition.deliveryNotes left join fetch settlementRequisition.jobSheets left join fetch settlementRequisition.signatures left join fetch settlementRequisition.businessDocuments left join fetch settlementRequisition.applicationMappings left join fetch settlementRequisition.placeholders left join fetch settlementRequisition.settlements"
    )
    List<SettlementRequisition> findAllWithEagerRelationships();

    @Query(
        "select settlementRequisition from SettlementRequisition settlementRequisition left join fetch settlementRequisition.paymentInvoices left join fetch settlementRequisition.deliveryNotes left join fetch settlementRequisition.jobSheets left join fetch settlementRequisition.signatures left join fetch settlementRequisition.businessDocuments left join fetch settlementRequisition.applicationMappings left join fetch settlementRequisition.placeholders left join fetch settlementRequisition.settlements where settlementRequisition.id =:id"
    )
    Optional<SettlementRequisition> findOneWithEagerRelationships(@Param("id") Long id);
}
