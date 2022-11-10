package io.github.erp.repository;

import io.github.erp.domain.CountyCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CountyCode entity.
 */
@Repository
public interface CountyCodeRepository extends JpaRepository<CountyCode, Long>, JpaSpecificationExecutor<CountyCode> {
    @Query(
        value = "select distinct countyCode from CountyCode countyCode left join fetch countyCode.placeholders",
        countQuery = "select count(distinct countyCode) from CountyCode countyCode"
    )
    Page<CountyCode> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct countyCode from CountyCode countyCode left join fetch countyCode.placeholders")
    List<CountyCode> findAllWithEagerRelationships();

    @Query("select countyCode from CountyCode countyCode left join fetch countyCode.placeholders where countyCode.id =:id")
    Optional<CountyCode> findOneWithEagerRelationships(@Param("id") Long id);
}
