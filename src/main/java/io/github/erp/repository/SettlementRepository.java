package io.github.erp.repository;

import io.github.erp.domain.Settlement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Settlement entity.
 */
@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long>, JpaSpecificationExecutor<Settlement> {
    @Query(
        value = "select distinct settlement from Settlement settlement left join fetch settlement.placeholders left join fetch settlement.paymentLabels left join fetch settlement.paymentInvoices left join fetch settlement.signatories left join fetch settlement.businessDocuments",
        countQuery = "select count(distinct settlement) from Settlement settlement"
    )
    Page<Settlement> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct settlement from Settlement settlement left join fetch settlement.placeholders left join fetch settlement.paymentLabels left join fetch settlement.paymentInvoices left join fetch settlement.signatories left join fetch settlement.businessDocuments"
    )
    List<Settlement> findAllWithEagerRelationships();

    @Query(
        "select settlement from Settlement settlement left join fetch settlement.placeholders left join fetch settlement.paymentLabels left join fetch settlement.paymentInvoices left join fetch settlement.signatories left join fetch settlement.businessDocuments where settlement.id =:id"
    )
    Optional<Settlement> findOneWithEagerRelationships(@Param("id") Long id);
}
