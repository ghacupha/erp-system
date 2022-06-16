package io.github.erp.repository;

import io.github.erp.domain.MfbBranchCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MfbBranchCode entity.
 */
@Repository
public interface MfbBranchCodeRepository extends JpaRepository<MfbBranchCode, Long>, JpaSpecificationExecutor<MfbBranchCode> {
    @Query(
        value = "select distinct mfbBranchCode from MfbBranchCode mfbBranchCode left join fetch mfbBranchCode.placeholders",
        countQuery = "select count(distinct mfbBranchCode) from MfbBranchCode mfbBranchCode"
    )
    Page<MfbBranchCode> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct mfbBranchCode from MfbBranchCode mfbBranchCode left join fetch mfbBranchCode.placeholders")
    List<MfbBranchCode> findAllWithEagerRelationships();

    @Query("select mfbBranchCode from MfbBranchCode mfbBranchCode left join fetch mfbBranchCode.placeholders where mfbBranchCode.id =:id")
    Optional<MfbBranchCode> findOneWithEagerRelationships(@Param("id") Long id);
}
