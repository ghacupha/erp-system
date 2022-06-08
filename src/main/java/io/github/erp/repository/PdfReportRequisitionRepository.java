package io.github.erp.repository;

import io.github.erp.domain.PdfReportRequisition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PdfReportRequisition entity.
 */
@Repository
public interface PdfReportRequisitionRepository
    extends JpaRepository<PdfReportRequisition, Long>, JpaSpecificationExecutor<PdfReportRequisition> {
    @Query(
        value = "select distinct pdfReportRequisition from PdfReportRequisition pdfReportRequisition left join fetch pdfReportRequisition.placeholders",
        countQuery = "select count(distinct pdfReportRequisition) from PdfReportRequisition pdfReportRequisition"
    )
    Page<PdfReportRequisition> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct pdfReportRequisition from PdfReportRequisition pdfReportRequisition left join fetch pdfReportRequisition.placeholders"
    )
    List<PdfReportRequisition> findAllWithEagerRelationships();

    @Query(
        "select pdfReportRequisition from PdfReportRequisition pdfReportRequisition left join fetch pdfReportRequisition.placeholders where pdfReportRequisition.id =:id"
    )
    Optional<PdfReportRequisition> findOneWithEagerRelationships(@Param("id") Long id);
}
