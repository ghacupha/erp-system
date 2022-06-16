package io.github.erp.repository;

import io.github.erp.domain.ReportRequisition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReportRequisition entity.
 */
@Repository
public interface ReportRequisitionRepository extends JpaRepository<ReportRequisition, Long>, JpaSpecificationExecutor<ReportRequisition> {
    @Query(
        value = "select distinct reportRequisition from ReportRequisition reportRequisition left join fetch reportRequisition.placeholders left join fetch reportRequisition.parameters",
        countQuery = "select count(distinct reportRequisition) from ReportRequisition reportRequisition"
    )
    Page<ReportRequisition> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct reportRequisition from ReportRequisition reportRequisition left join fetch reportRequisition.placeholders left join fetch reportRequisition.parameters"
    )
    List<ReportRequisition> findAllWithEagerRelationships();

    @Query(
        "select reportRequisition from ReportRequisition reportRequisition left join fetch reportRequisition.placeholders left join fetch reportRequisition.parameters where reportRequisition.id =:id"
    )
    Optional<ReportRequisition> findOneWithEagerRelationships(@Param("id") Long id);
}
