package io.github.erp.repository;

import io.github.erp.domain.DepreciationMethod;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DepreciationMethod entity.
 */
@Repository
public interface DepreciationMethodRepository
    extends JpaRepository<DepreciationMethod, Long>, JpaSpecificationExecutor<DepreciationMethod> {
    @Query(
        value = "select distinct depreciationMethod from DepreciationMethod depreciationMethod left join fetch depreciationMethod.placeholders",
        countQuery = "select count(distinct depreciationMethod) from DepreciationMethod depreciationMethod"
    )
    Page<DepreciationMethod> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct depreciationMethod from DepreciationMethod depreciationMethod left join fetch depreciationMethod.placeholders")
    List<DepreciationMethod> findAllWithEagerRelationships();

    @Query(
        "select depreciationMethod from DepreciationMethod depreciationMethod left join fetch depreciationMethod.placeholders where depreciationMethod.id =:id"
    )
    Optional<DepreciationMethod> findOneWithEagerRelationships(@Param("id") Long id);
}
