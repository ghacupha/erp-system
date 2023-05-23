package io.github.erp.repository;

import io.github.erp.domain.AmortizationRecurrence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AmortizationRecurrence entity.
 */
@Repository
public interface AmortizationRecurrenceRepository
    extends JpaRepository<AmortizationRecurrence, Long>, JpaSpecificationExecutor<AmortizationRecurrence> {
    @Query(
        value = "select distinct amortizationRecurrence from AmortizationRecurrence amortizationRecurrence left join fetch amortizationRecurrence.placeholders left join fetch amortizationRecurrence.parameters left join fetch amortizationRecurrence.applicationParameters",
        countQuery = "select count(distinct amortizationRecurrence) from AmortizationRecurrence amortizationRecurrence"
    )
    Page<AmortizationRecurrence> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct amortizationRecurrence from AmortizationRecurrence amortizationRecurrence left join fetch amortizationRecurrence.placeholders left join fetch amortizationRecurrence.parameters left join fetch amortizationRecurrence.applicationParameters"
    )
    List<AmortizationRecurrence> findAllWithEagerRelationships();

    @Query(
        "select amortizationRecurrence from AmortizationRecurrence amortizationRecurrence left join fetch amortizationRecurrence.placeholders left join fetch amortizationRecurrence.parameters left join fetch amortizationRecurrence.applicationParameters where amortizationRecurrence.id =:id"
    )
    Optional<AmortizationRecurrence> findOneWithEagerRelationships(@Param("id") Long id);
}
