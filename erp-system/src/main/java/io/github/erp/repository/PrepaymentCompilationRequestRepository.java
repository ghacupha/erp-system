package io.github.erp.repository;

import io.github.erp.domain.PrepaymentCompilationRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PrepaymentCompilationRequest entity.
 */
@Repository
public interface PrepaymentCompilationRequestRepository
    extends JpaRepository<PrepaymentCompilationRequest, Long>, JpaSpecificationExecutor<PrepaymentCompilationRequest> {
    @Query(
        value = "select distinct prepaymentCompilationRequest from PrepaymentCompilationRequest prepaymentCompilationRequest left join fetch prepaymentCompilationRequest.placeholders",
        countQuery = "select count(distinct prepaymentCompilationRequest) from PrepaymentCompilationRequest prepaymentCompilationRequest"
    )
    Page<PrepaymentCompilationRequest> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct prepaymentCompilationRequest from PrepaymentCompilationRequest prepaymentCompilationRequest left join fetch prepaymentCompilationRequest.placeholders"
    )
    List<PrepaymentCompilationRequest> findAllWithEagerRelationships();

    @Query(
        "select prepaymentCompilationRequest from PrepaymentCompilationRequest prepaymentCompilationRequest left join fetch prepaymentCompilationRequest.placeholders where prepaymentCompilationRequest.id =:id"
    )
    Optional<PrepaymentCompilationRequest> findOneWithEagerRelationships(@Param("id") Long id);
}
