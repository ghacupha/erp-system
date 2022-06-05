package io.github.erp.repository;

import io.github.erp.domain.XlsxReportRequisition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the XlsxReportRequisition entity.
 */
@Repository
public interface XlsxReportRequisitionRepository
    extends JpaRepository<XlsxReportRequisition, Long>, JpaSpecificationExecutor<XlsxReportRequisition> {
    @Query(
        value = "select distinct xlsxReportRequisition from XlsxReportRequisition xlsxReportRequisition left join fetch xlsxReportRequisition.placeholders",
        countQuery = "select count(distinct xlsxReportRequisition) from XlsxReportRequisition xlsxReportRequisition"
    )
    Page<XlsxReportRequisition> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct xlsxReportRequisition from XlsxReportRequisition xlsxReportRequisition left join fetch xlsxReportRequisition.placeholders"
    )
    List<XlsxReportRequisition> findAllWithEagerRelationships();

    @Query(
        "select xlsxReportRequisition from XlsxReportRequisition xlsxReportRequisition left join fetch xlsxReportRequisition.placeholders where xlsxReportRequisition.id =:id"
    )
    Optional<XlsxReportRequisition> findOneWithEagerRelationships(@Param("id") Long id);
}
