package io.github.erp.repository;

import io.github.erp.domain.TaxRule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TaxRule entity.
 */
@Repository
public interface TaxRuleRepository extends JpaRepository<TaxRule, Long>, JpaSpecificationExecutor<TaxRule> {
    @Query(
        value = "select distinct taxRule from TaxRule taxRule left join fetch taxRule.placeholders",
        countQuery = "select count(distinct taxRule) from TaxRule taxRule"
    )
    Page<TaxRule> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct taxRule from TaxRule taxRule left join fetch taxRule.placeholders")
    List<TaxRule> findAllWithEagerRelationships();

    @Query("select taxRule from TaxRule taxRule left join fetch taxRule.placeholders where taxRule.id =:id")
    Optional<TaxRule> findOneWithEagerRelationships(@Param("id") Long id);
}
