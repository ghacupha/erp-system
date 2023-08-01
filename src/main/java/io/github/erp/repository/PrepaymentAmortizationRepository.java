package io.github.erp.repository;

import io.github.erp.domain.PrepaymentAmortization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PrepaymentAmortization entity.
 */
@Repository
public interface PrepaymentAmortizationRepository
    extends JpaRepository<PrepaymentAmortization, Long>, JpaSpecificationExecutor<PrepaymentAmortization> {
    @Query(
        value = "select distinct prepaymentAmortization from PrepaymentAmortization prepaymentAmortization left join fetch prepaymentAmortization.placeholders",
        countQuery = "select count(distinct prepaymentAmortization) from PrepaymentAmortization prepaymentAmortization"
    )
    Page<PrepaymentAmortization> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct prepaymentAmortization from PrepaymentAmortization prepaymentAmortization left join fetch prepaymentAmortization.placeholders"
    )
    List<PrepaymentAmortization> findAllWithEagerRelationships();

    @Query(
        "select prepaymentAmortization from PrepaymentAmortization prepaymentAmortization left join fetch prepaymentAmortization.placeholders where prepaymentAmortization.id =:id"
    )
    Optional<PrepaymentAmortization> findOneWithEagerRelationships(@Param("id") Long id);
}
