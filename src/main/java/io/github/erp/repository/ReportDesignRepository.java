package io.github.erp.repository;

import io.github.erp.domain.ReportDesign;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReportDesign entity.
 */
@Repository
public interface ReportDesignRepository extends JpaRepository<ReportDesign, Long>, JpaSpecificationExecutor<ReportDesign> {
    @Query(
        value = "select distinct reportDesign from ReportDesign reportDesign left join fetch reportDesign.parameters left join fetch reportDesign.placeholders",
        countQuery = "select count(distinct reportDesign) from ReportDesign reportDesign"
    )
    Page<ReportDesign> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct reportDesign from ReportDesign reportDesign left join fetch reportDesign.parameters left join fetch reportDesign.placeholders"
    )
    List<ReportDesign> findAllWithEagerRelationships();

    @Query(
        "select reportDesign from ReportDesign reportDesign left join fetch reportDesign.parameters left join fetch reportDesign.placeholders where reportDesign.id =:id"
    )
    Optional<ReportDesign> findOneWithEagerRelationships(@Param("id") Long id);
}
