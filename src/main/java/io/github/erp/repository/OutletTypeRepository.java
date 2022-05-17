package io.github.erp.repository;

import io.github.erp.domain.OutletType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OutletType entity.
 */
@Repository
public interface OutletTypeRepository extends JpaRepository<OutletType, Long>, JpaSpecificationExecutor<OutletType> {
    @Query(
        value = "select distinct outletType from OutletType outletType left join fetch outletType.placeholders",
        countQuery = "select count(distinct outletType) from OutletType outletType"
    )
    Page<OutletType> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct outletType from OutletType outletType left join fetch outletType.placeholders")
    List<OutletType> findAllWithEagerRelationships();

    @Query("select outletType from OutletType outletType left join fetch outletType.placeholders where outletType.id =:id")
    Optional<OutletType> findOneWithEagerRelationships(@Param("id") Long id);
}
