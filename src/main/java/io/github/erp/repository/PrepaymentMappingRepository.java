package io.github.erp.repository;

import io.github.erp.domain.PrepaymentMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PrepaymentMapping entity.
 */
@Repository
public interface PrepaymentMappingRepository extends JpaRepository<PrepaymentMapping, Long>, JpaSpecificationExecutor<PrepaymentMapping> {
    @Query(
        value = "select distinct prepaymentMapping from PrepaymentMapping prepaymentMapping left join fetch prepaymentMapping.placeholders",
        countQuery = "select count(distinct prepaymentMapping) from PrepaymentMapping prepaymentMapping"
    )
    Page<PrepaymentMapping> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct prepaymentMapping from PrepaymentMapping prepaymentMapping left join fetch prepaymentMapping.placeholders")
    List<PrepaymentMapping> findAllWithEagerRelationships();

    @Query(
        "select prepaymentMapping from PrepaymentMapping prepaymentMapping left join fetch prepaymentMapping.placeholders where prepaymentMapping.id =:id"
    )
    Optional<PrepaymentMapping> findOneWithEagerRelationships(@Param("id") Long id);
}
