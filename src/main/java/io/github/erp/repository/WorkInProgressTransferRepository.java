package io.github.erp.repository;

import io.github.erp.domain.WorkInProgressTransfer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkInProgressTransfer entity.
 */
@Repository
public interface WorkInProgressTransferRepository
    extends JpaRepository<WorkInProgressTransfer, Long>, JpaSpecificationExecutor<WorkInProgressTransfer> {
    @Query(
        value = "select distinct workInProgressTransfer from WorkInProgressTransfer workInProgressTransfer left join fetch workInProgressTransfer.workInProgressRegistrations left join fetch workInProgressTransfer.placeholders left join fetch workInProgressTransfer.businessDocuments",
        countQuery = "select count(distinct workInProgressTransfer) from WorkInProgressTransfer workInProgressTransfer"
    )
    Page<WorkInProgressTransfer> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct workInProgressTransfer from WorkInProgressTransfer workInProgressTransfer left join fetch workInProgressTransfer.workInProgressRegistrations left join fetch workInProgressTransfer.placeholders left join fetch workInProgressTransfer.businessDocuments"
    )
    List<WorkInProgressTransfer> findAllWithEagerRelationships();

    @Query(
        "select workInProgressTransfer from WorkInProgressTransfer workInProgressTransfer left join fetch workInProgressTransfer.workInProgressRegistrations left join fetch workInProgressTransfer.placeholders left join fetch workInProgressTransfer.businessDocuments where workInProgressTransfer.id =:id"
    )
    Optional<WorkInProgressTransfer> findOneWithEagerRelationships(@Param("id") Long id);
}
