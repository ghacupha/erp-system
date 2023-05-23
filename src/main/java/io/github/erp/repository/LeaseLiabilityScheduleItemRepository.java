package io.github.erp.repository;

import io.github.erp.domain.LeaseLiabilityScheduleItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LeaseLiabilityScheduleItem entity.
 */
@Repository
public interface LeaseLiabilityScheduleItemRepository
    extends JpaRepository<LeaseLiabilityScheduleItem, Long>, JpaSpecificationExecutor<LeaseLiabilityScheduleItem> {
    @Query(
        value = "select distinct leaseLiabilityScheduleItem from LeaseLiabilityScheduleItem leaseLiabilityScheduleItem left join fetch leaseLiabilityScheduleItem.placeholders left join fetch leaseLiabilityScheduleItem.universallyUniqueMappings",
        countQuery = "select count(distinct leaseLiabilityScheduleItem) from LeaseLiabilityScheduleItem leaseLiabilityScheduleItem"
    )
    Page<LeaseLiabilityScheduleItem> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct leaseLiabilityScheduleItem from LeaseLiabilityScheduleItem leaseLiabilityScheduleItem left join fetch leaseLiabilityScheduleItem.placeholders left join fetch leaseLiabilityScheduleItem.universallyUniqueMappings"
    )
    List<LeaseLiabilityScheduleItem> findAllWithEagerRelationships();

    @Query(
        "select leaseLiabilityScheduleItem from LeaseLiabilityScheduleItem leaseLiabilityScheduleItem left join fetch leaseLiabilityScheduleItem.placeholders left join fetch leaseLiabilityScheduleItem.universallyUniqueMappings where leaseLiabilityScheduleItem.id =:id"
    )
    Optional<LeaseLiabilityScheduleItem> findOneWithEagerRelationships(@Param("id") Long id);
}
