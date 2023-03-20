package io.github.erp.repository;

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
