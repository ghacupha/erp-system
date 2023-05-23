package io.github.erp.repository;

import io.github.erp.domain.ReportStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReportStatus entity.
 */
@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, Long>, JpaSpecificationExecutor<ReportStatus> {
    @Query(
        value = "select distinct reportStatus from ReportStatus reportStatus left join fetch reportStatus.placeholders",
        countQuery = "select count(distinct reportStatus) from ReportStatus reportStatus"
    )
    Page<ReportStatus> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct reportStatus from ReportStatus reportStatus left join fetch reportStatus.placeholders")
    List<ReportStatus> findAllWithEagerRelationships();

    @Query("select reportStatus from ReportStatus reportStatus left join fetch reportStatus.placeholders where reportStatus.id =:id")
    Optional<ReportStatus> findOneWithEagerRelationships(@Param("id") Long id);
}
