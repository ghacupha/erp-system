package io.github.erp.repository;

import io.github.erp.domain.SystemContentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SystemContentType entity.
 */
@Repository
public interface SystemContentTypeRepository extends JpaRepository<SystemContentType, Long>, JpaSpecificationExecutor<SystemContentType> {
    @Query(
        value = "select distinct systemContentType from SystemContentType systemContentType left join fetch systemContentType.placeholders left join fetch systemContentType.sysMaps",
        countQuery = "select count(distinct systemContentType) from SystemContentType systemContentType"
    )
    Page<SystemContentType> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct systemContentType from SystemContentType systemContentType left join fetch systemContentType.placeholders left join fetch systemContentType.sysMaps"
    )
    List<SystemContentType> findAllWithEagerRelationships();

    @Query(
        "select systemContentType from SystemContentType systemContentType left join fetch systemContentType.placeholders left join fetch systemContentType.sysMaps where systemContentType.id =:id"
    )
    Optional<SystemContentType> findOneWithEagerRelationships(@Param("id") Long id);
}
