package io.github.erp.repository;

import io.github.erp.domain.ReportTemplate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReportTemplate entity.
 */
@Repository
public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, Long>, JpaSpecificationExecutor<ReportTemplate> {
    @Query(
        value = "select distinct reportTemplate from ReportTemplate reportTemplate left join fetch reportTemplate.placeholders",
        countQuery = "select count(distinct reportTemplate) from ReportTemplate reportTemplate"
    )
    Page<ReportTemplate> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct reportTemplate from ReportTemplate reportTemplate left join fetch reportTemplate.placeholders")
    List<ReportTemplate> findAllWithEagerRelationships();

    @Query(
        "select reportTemplate from ReportTemplate reportTemplate left join fetch reportTemplate.placeholders where reportTemplate.id =:id"
    )
    Optional<ReportTemplate> findOneWithEagerRelationships(@Param("id") Long id);
}
