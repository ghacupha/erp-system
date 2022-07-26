package io.github.erp.repository;

import io.github.erp.domain.SettlementCurrency;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SettlementCurrency entity.
 */
@Repository
public interface SettlementCurrencyRepository
    extends JpaRepository<SettlementCurrency, Long>, JpaSpecificationExecutor<SettlementCurrency> {
    @Query(
        value = "select distinct settlementCurrency from SettlementCurrency settlementCurrency left join fetch settlementCurrency.placeholders",
        countQuery = "select count(distinct settlementCurrency) from SettlementCurrency settlementCurrency"
    )
    Page<SettlementCurrency> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct settlementCurrency from SettlementCurrency settlementCurrency left join fetch settlementCurrency.placeholders")
    List<SettlementCurrency> findAllWithEagerRelationships();

    @Query(
        "select settlementCurrency from SettlementCurrency settlementCurrency left join fetch settlementCurrency.placeholders where settlementCurrency.id =:id"
    )
    Optional<SettlementCurrency> findOneWithEagerRelationships(@Param("id") Long id);
}
