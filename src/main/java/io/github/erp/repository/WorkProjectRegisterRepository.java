package io.github.erp.repository;

import io.github.erp.domain.WorkProjectRegister;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkProjectRegister entity.
 */
@Repository
public interface WorkProjectRegisterRepository
    extends JpaRepository<WorkProjectRegister, Long>, JpaSpecificationExecutor<WorkProjectRegister> {
    @Query(
        value = "select distinct workProjectRegister from WorkProjectRegister workProjectRegister left join fetch workProjectRegister.dealers left join fetch workProjectRegister.placeholders",
        countQuery = "select count(distinct workProjectRegister) from WorkProjectRegister workProjectRegister"
    )
    Page<WorkProjectRegister> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct workProjectRegister from WorkProjectRegister workProjectRegister left join fetch workProjectRegister.dealers left join fetch workProjectRegister.placeholders"
    )
    List<WorkProjectRegister> findAllWithEagerRelationships();

    @Query(
        "select workProjectRegister from WorkProjectRegister workProjectRegister left join fetch workProjectRegister.dealers left join fetch workProjectRegister.placeholders where workProjectRegister.id =:id"
    )
    Optional<WorkProjectRegister> findOneWithEagerRelationships(@Param("id") Long id);
}
