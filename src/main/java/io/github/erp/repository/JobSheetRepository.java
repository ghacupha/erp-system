package io.github.erp.repository;

import io.github.erp.domain.JobSheet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the JobSheet entity.
 */
@Repository
public interface JobSheetRepository extends JpaRepository<JobSheet, Long>, JpaSpecificationExecutor<JobSheet> {
    @Query(
        value = "select distinct jobSheet from JobSheet jobSheet left join fetch jobSheet.signatories left join fetch jobSheet.businessStamps left join fetch jobSheet.placeholders left join fetch jobSheet.paymentLabels",
        countQuery = "select count(distinct jobSheet) from JobSheet jobSheet"
    )
    Page<JobSheet> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct jobSheet from JobSheet jobSheet left join fetch jobSheet.signatories left join fetch jobSheet.businessStamps left join fetch jobSheet.placeholders left join fetch jobSheet.paymentLabels"
    )
    List<JobSheet> findAllWithEagerRelationships();

    @Query(
        "select jobSheet from JobSheet jobSheet left join fetch jobSheet.signatories left join fetch jobSheet.businessStamps left join fetch jobSheet.placeholders left join fetch jobSheet.paymentLabels where jobSheet.id =:id"
    )
    Optional<JobSheet> findOneWithEagerRelationships(@Param("id") Long id);
}
