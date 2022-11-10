package io.github.erp.repository;

import io.github.erp.domain.ReportContentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReportContentType entity.
 */
@Repository
public interface ReportContentTypeRepository extends JpaRepository<ReportContentType, Long>, JpaSpecificationExecutor<ReportContentType> {
    @Query(
        value = "select distinct reportContentType from ReportContentType reportContentType left join fetch reportContentType.placeholders",
        countQuery = "select count(distinct reportContentType) from ReportContentType reportContentType"
    )
    Page<ReportContentType> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct reportContentType from ReportContentType reportContentType left join fetch reportContentType.placeholders")
    List<ReportContentType> findAllWithEagerRelationships();

    @Query(
        "select reportContentType from ReportContentType reportContentType left join fetch reportContentType.placeholders where reportContentType.id =:id"
    )
    Optional<ReportContentType> findOneWithEagerRelationships(@Param("id") Long id);
}
