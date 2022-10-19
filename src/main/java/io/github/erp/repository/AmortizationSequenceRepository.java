package io.github.erp.repository;

import io.github.erp.domain.AmortizationSequence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AmortizationSequence entity.
 */
@Repository
public interface AmortizationSequenceRepository
    extends JpaRepository<AmortizationSequence, Long>, JpaSpecificationExecutor<AmortizationSequence> {
    @Query(
        value = "select distinct amortizationSequence from AmortizationSequence amortizationSequence left join fetch amortizationSequence.placeholders left join fetch amortizationSequence.prepaymentMappings left join fetch amortizationSequence.applicationParameters",
        countQuery = "select count(distinct amortizationSequence) from AmortizationSequence amortizationSequence"
    )
    Page<AmortizationSequence> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct amortizationSequence from AmortizationSequence amortizationSequence left join fetch amortizationSequence.placeholders left join fetch amortizationSequence.prepaymentMappings left join fetch amortizationSequence.applicationParameters"
    )
    List<AmortizationSequence> findAllWithEagerRelationships();

    @Query(
        "select amortizationSequence from AmortizationSequence amortizationSequence left join fetch amortizationSequence.placeholders left join fetch amortizationSequence.prepaymentMappings left join fetch amortizationSequence.applicationParameters where amortizationSequence.id =:id"
    )
    Optional<AmortizationSequence> findOneWithEagerRelationships(@Param("id") Long id);
}
