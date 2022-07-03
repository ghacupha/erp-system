package io.github.erp.repository;

import io.github.erp.domain.ServiceOutlet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ServiceOutlet entity.
 */
@Repository
public interface ServiceOutletRepository extends JpaRepository<ServiceOutlet, Long>, JpaSpecificationExecutor<ServiceOutlet> {
    @Query(
        value = "select distinct serviceOutlet from ServiceOutlet serviceOutlet left join fetch serviceOutlet.placeholders",
        countQuery = "select count(distinct serviceOutlet) from ServiceOutlet serviceOutlet"
    )
    Page<ServiceOutlet> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct serviceOutlet from ServiceOutlet serviceOutlet left join fetch serviceOutlet.placeholders")
    List<ServiceOutlet> findAllWithEagerRelationships();

    @Query("select serviceOutlet from ServiceOutlet serviceOutlet left join fetch serviceOutlet.placeholders where serviceOutlet.id =:id")
    Optional<ServiceOutlet> findOneWithEagerRelationships(@Param("id") Long id);
}
