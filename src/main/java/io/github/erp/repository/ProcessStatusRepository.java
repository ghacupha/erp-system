package io.github.erp.repository;

import io.github.erp.domain.ProcessStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProcessStatus entity.
 */
@Repository
public interface ProcessStatusRepository extends JpaRepository<ProcessStatus, Long>, JpaSpecificationExecutor<ProcessStatus> {
    @Query(
        value = "select distinct processStatus from ProcessStatus processStatus left join fetch processStatus.placeholders left join fetch processStatus.parameters",
        countQuery = "select count(distinct processStatus) from ProcessStatus processStatus"
    )
    Page<ProcessStatus> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct processStatus from ProcessStatus processStatus left join fetch processStatus.placeholders left join fetch processStatus.parameters"
    )
    List<ProcessStatus> findAllWithEagerRelationships();

    @Query(
        "select processStatus from ProcessStatus processStatus left join fetch processStatus.placeholders left join fetch processStatus.parameters where processStatus.id =:id"
    )
    Optional<ProcessStatus> findOneWithEagerRelationships(@Param("id") Long id);
}
