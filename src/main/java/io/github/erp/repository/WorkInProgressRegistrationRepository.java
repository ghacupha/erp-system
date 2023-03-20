package io.github.erp.repository;

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
        value = "select distinct workInProgressRegistration from WorkInProgressRegistration workInProgressRegistration left join fetch workInProgressRegistration.placeholders left join fetch workInProgressRegistration.paymentInvoices left join fetch workInProgressRegistration.serviceOutlets left join fetch workInProgressRegistration.settlements left join fetch workInProgressRegistration.purchaseOrders left join fetch workInProgressRegistration.deliveryNotes left join fetch workInProgressRegistration.jobSheets left join fetch workInProgressRegistration.businessDocuments",
        countQuery = "select count(distinct workInProgressRegistration) from WorkInProgressRegistration workInProgressRegistration"
    )
    Page<WorkInProgressRegistration> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct workInProgressRegistration from WorkInProgressRegistration workInProgressRegistration left join fetch workInProgressRegistration.placeholders left join fetch workInProgressRegistration.paymentInvoices left join fetch workInProgressRegistration.serviceOutlets left join fetch workInProgressRegistration.settlements left join fetch workInProgressRegistration.purchaseOrders left join fetch workInProgressRegistration.deliveryNotes left join fetch workInProgressRegistration.jobSheets left join fetch workInProgressRegistration.businessDocuments"
    )
    List<WorkInProgressRegistration> findAllWithEagerRelationships();

    @Query(
        "select workInProgressRegistration from WorkInProgressRegistration workInProgressRegistration left join fetch workInProgressRegistration.placeholders left join fetch workInProgressRegistration.paymentInvoices left join fetch workInProgressRegistration.serviceOutlets left join fetch workInProgressRegistration.settlements left join fetch workInProgressRegistration.purchaseOrders left join fetch workInProgressRegistration.deliveryNotes left join fetch workInProgressRegistration.jobSheets left join fetch workInProgressRegistration.businessDocuments where workInProgressRegistration.id =:id"
    )
    Optional<WorkInProgressRegistration> findOneWithEagerRelationships(@Param("id") Long id);
}
