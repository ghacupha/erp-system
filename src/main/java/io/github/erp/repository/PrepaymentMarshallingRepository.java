package io.github.erp.repository;

import io.github.erp.domain.PrepaymentMarshalling;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PrepaymentMarshalling entity.
 */
@Repository
public interface PrepaymentMarshallingRepository
    extends JpaRepository<PrepaymentMarshalling, Long>, JpaSpecificationExecutor<PrepaymentMarshalling> {
    @Query(
        value = "select distinct prepaymentMarshalling from PrepaymentMarshalling prepaymentMarshalling left join fetch prepaymentMarshalling.placeholders",
        countQuery = "select count(distinct prepaymentMarshalling) from PrepaymentMarshalling prepaymentMarshalling"
    )
    Page<PrepaymentMarshalling> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct prepaymentMarshalling from PrepaymentMarshalling prepaymentMarshalling left join fetch prepaymentMarshalling.placeholders"
    )
    List<PrepaymentMarshalling> findAllWithEagerRelationships();

    @Query(
        "select prepaymentMarshalling from PrepaymentMarshalling prepaymentMarshalling left join fetch prepaymentMarshalling.placeholders where prepaymentMarshalling.id =:id"
    )
    Optional<PrepaymentMarshalling> findOneWithEagerRelationships(@Param("id") Long id);
}
