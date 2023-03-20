package io.github.erp.repository;

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
