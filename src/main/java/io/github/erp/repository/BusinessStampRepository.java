package io.github.erp.repository;

import io.github.erp.domain.BusinessStamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusinessStamp entity.
 */
@Repository
public interface BusinessStampRepository extends JpaRepository<BusinessStamp, Long>, JpaSpecificationExecutor<BusinessStamp> {
    @Query(
        value = "select distinct businessStamp from BusinessStamp businessStamp left join fetch businessStamp.placeholders",
        countQuery = "select count(distinct businessStamp) from BusinessStamp businessStamp"
    )
    Page<BusinessStamp> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct businessStamp from BusinessStamp businessStamp left join fetch businessStamp.placeholders")
    List<BusinessStamp> findAllWithEagerRelationships();

    @Query("select businessStamp from BusinessStamp businessStamp left join fetch businessStamp.placeholders where businessStamp.id =:id")
    Optional<BusinessStamp> findOneWithEagerRelationships(@Param("id") Long id);
}
