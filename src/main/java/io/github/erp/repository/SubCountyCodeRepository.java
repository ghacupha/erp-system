package io.github.erp.repository;

import io.github.erp.domain.SubCountyCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubCountyCode entity.
 */
@Repository
public interface SubCountyCodeRepository extends JpaRepository<SubCountyCode, Long>, JpaSpecificationExecutor<SubCountyCode> {
    @Query(
        value = "select distinct subCountyCode from SubCountyCode subCountyCode left join fetch subCountyCode.placeholders",
        countQuery = "select count(distinct subCountyCode) from SubCountyCode subCountyCode"
    )
    Page<SubCountyCode> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct subCountyCode from SubCountyCode subCountyCode left join fetch subCountyCode.placeholders")
    List<SubCountyCode> findAllWithEagerRelationships();

    @Query("select subCountyCode from SubCountyCode subCountyCode left join fetch subCountyCode.placeholders where subCountyCode.id =:id")
    Optional<SubCountyCode> findOneWithEagerRelationships(@Param("id") Long id);
}
