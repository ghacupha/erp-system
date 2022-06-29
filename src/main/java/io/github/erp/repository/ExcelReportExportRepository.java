package io.github.erp.repository;

import io.github.erp.domain.ExcelReportExport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExcelReportExport entity.
 */
@Repository
public interface ExcelReportExportRepository extends JpaRepository<ExcelReportExport, Long>, JpaSpecificationExecutor<ExcelReportExport> {
    @Query(
        value = "select distinct excelReportExport from ExcelReportExport excelReportExport left join fetch excelReportExport.placeholders",
        countQuery = "select count(distinct excelReportExport) from ExcelReportExport excelReportExport"
    )
    Page<ExcelReportExport> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct excelReportExport from ExcelReportExport excelReportExport left join fetch excelReportExport.placeholders")
    List<ExcelReportExport> findAllWithEagerRelationships();

    @Query(
        "select excelReportExport from ExcelReportExport excelReportExport left join fetch excelReportExport.placeholders where excelReportExport.id =:id"
    )
    Optional<ExcelReportExport> findOneWithEagerRelationships(@Param("id") Long id);
}
