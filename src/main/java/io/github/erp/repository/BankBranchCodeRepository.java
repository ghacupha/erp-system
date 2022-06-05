package io.github.erp.repository;

import io.github.erp.domain.BankBranchCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BankBranchCode entity.
 */
@Repository
public interface BankBranchCodeRepository extends JpaRepository<BankBranchCode, Long>, JpaSpecificationExecutor<BankBranchCode> {
    @Query(
        value = "select distinct bankBranchCode from BankBranchCode bankBranchCode left join fetch bankBranchCode.placeholders",
        countQuery = "select count(distinct bankBranchCode) from BankBranchCode bankBranchCode"
    )
    Page<BankBranchCode> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct bankBranchCode from BankBranchCode bankBranchCode left join fetch bankBranchCode.placeholders")
    List<BankBranchCode> findAllWithEagerRelationships();

    @Query(
        "select bankBranchCode from BankBranchCode bankBranchCode left join fetch bankBranchCode.placeholders where bankBranchCode.id =:id"
    )
    Optional<BankBranchCode> findOneWithEagerRelationships(@Param("id") Long id);
}
