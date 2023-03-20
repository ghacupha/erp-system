package io.github.erp.repository;

import io.github.erp.domain.OutletStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OutletStatus entity.
 */
@Repository
public interface OutletStatusRepository extends JpaRepository<OutletStatus, Long>, JpaSpecificationExecutor<OutletStatus> {
    @Query(
        value = "select distinct outletStatus from OutletStatus outletStatus left join fetch outletStatus.placeholders",
        countQuery = "select count(distinct outletStatus) from OutletStatus outletStatus"
    )
    Page<OutletStatus> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct outletStatus from OutletStatus outletStatus left join fetch outletStatus.placeholders")
    List<OutletStatus> findAllWithEagerRelationships();

    @Query("select outletStatus from OutletStatus outletStatus left join fetch outletStatus.placeholders where outletStatus.id =:id")
    Optional<OutletStatus> findOneWithEagerRelationships(@Param("id") Long id);
}
