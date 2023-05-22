package io.github.erp.repository;

import io.github.erp.domain.SystemModule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SystemModule entity.
 */
@Repository
public interface SystemModuleRepository extends JpaRepository<SystemModule, Long>, JpaSpecificationExecutor<SystemModule> {
    @Query(
        value = "select distinct systemModule from SystemModule systemModule left join fetch systemModule.placeholders left join fetch systemModule.applicationMappings",
        countQuery = "select count(distinct systemModule) from SystemModule systemModule"
    )
    Page<SystemModule> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct systemModule from SystemModule systemModule left join fetch systemModule.placeholders left join fetch systemModule.applicationMappings"
    )
    List<SystemModule> findAllWithEagerRelationships();

    @Query(
        "select systemModule from SystemModule systemModule left join fetch systemModule.placeholders left join fetch systemModule.applicationMappings where systemModule.id =:id"
    )
    Optional<SystemModule> findOneWithEagerRelationships(@Param("id") Long id);
}
