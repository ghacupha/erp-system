package io.github.erp.repository;

import io.github.erp.domain.SecurityClearance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SecurityClearance entity.
 */
@Repository
public interface SecurityClearanceRepository extends JpaRepository<SecurityClearance, Long>, JpaSpecificationExecutor<SecurityClearance> {
    @Query(
        value = "select distinct securityClearance from SecurityClearance securityClearance left join fetch securityClearance.grantedClearances left join fetch securityClearance.placeholders",
        countQuery = "select count(distinct securityClearance) from SecurityClearance securityClearance"
    )
    Page<SecurityClearance> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct securityClearance from SecurityClearance securityClearance left join fetch securityClearance.grantedClearances left join fetch securityClearance.placeholders"
    )
    List<SecurityClearance> findAllWithEagerRelationships();

    @Query(
        "select securityClearance from SecurityClearance securityClearance left join fetch securityClearance.grantedClearances left join fetch securityClearance.placeholders where securityClearance.id =:id"
    )
    Optional<SecurityClearance> findOneWithEagerRelationships(@Param("id") Long id);
}
