package io.github.erp.repository;

import io.github.erp.domain.LeaseContract;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LeaseContract entity.
 */
@Repository
public interface LeaseContractRepository extends JpaRepository<LeaseContract, Long>, JpaSpecificationExecutor<LeaseContract> {
    @Query(
        value = "select distinct leaseContract from LeaseContract leaseContract left join fetch leaseContract.placeholders left join fetch leaseContract.systemMappings left join fetch leaseContract.businessDocuments left join fetch leaseContract.contractMetadata",
        countQuery = "select count(distinct leaseContract) from LeaseContract leaseContract"
    )
    Page<LeaseContract> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct leaseContract from LeaseContract leaseContract left join fetch leaseContract.placeholders left join fetch leaseContract.systemMappings left join fetch leaseContract.businessDocuments left join fetch leaseContract.contractMetadata"
    )
    List<LeaseContract> findAllWithEagerRelationships();

    @Query(
        "select leaseContract from LeaseContract leaseContract left join fetch leaseContract.placeholders left join fetch leaseContract.systemMappings left join fetch leaseContract.businessDocuments left join fetch leaseContract.contractMetadata where leaseContract.id =:id"
    )
    Optional<LeaseContract> findOneWithEagerRelationships(@Param("id") Long id);
}
