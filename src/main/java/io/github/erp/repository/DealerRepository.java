package io.github.erp.repository;

import io.github.erp.domain.Dealer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dealer entity.
 */
@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long>, JpaSpecificationExecutor<Dealer> {
    @Query(
        value = "select distinct dealer from Dealer dealer left join fetch dealer.paymentLabels left join fetch dealer.placeholders",
        countQuery = "select count(distinct dealer) from Dealer dealer"
    )
    Page<Dealer> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct dealer from Dealer dealer left join fetch dealer.paymentLabels left join fetch dealer.placeholders")
    List<Dealer> findAllWithEagerRelationships();

    @Query("select dealer from Dealer dealer left join fetch dealer.paymentLabels left join fetch dealer.placeholders where dealer.id =:id")
    Optional<Dealer> findOneWithEagerRelationships(@Param("id") Long id);
}
