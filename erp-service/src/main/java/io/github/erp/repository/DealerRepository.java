package io.github.erp.repository;

import io.github.erp.domain.Dealer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Dealer entity.
 */
@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long>, JpaSpecificationExecutor<Dealer> {

    @Query(value = "select distinct dealer from Dealer dealer left join fetch dealer.payments",
        countQuery = "select count(distinct dealer) from Dealer dealer")
    Page<Dealer> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct dealer from Dealer dealer left join fetch dealer.payments")
    List<Dealer> findAllWithEagerRelationships();

    @Query("select dealer from Dealer dealer left join fetch dealer.payments where dealer.id =:id")
    Optional<Dealer> findOneWithEagerRelationships(@Param("id") Long id);
}
