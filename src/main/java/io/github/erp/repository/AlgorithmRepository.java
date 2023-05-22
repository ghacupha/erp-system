package io.github.erp.repository;

import io.github.erp.domain.Algorithm;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Algorithm entity.
 */
@Repository
public interface AlgorithmRepository extends JpaRepository<Algorithm, Long>, JpaSpecificationExecutor<Algorithm> {
    @Query(
        value = "select distinct algorithm from Algorithm algorithm left join fetch algorithm.placeholders left join fetch algorithm.parameters",
        countQuery = "select count(distinct algorithm) from Algorithm algorithm"
    )
    Page<Algorithm> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct algorithm from Algorithm algorithm left join fetch algorithm.placeholders left join fetch algorithm.parameters")
    List<Algorithm> findAllWithEagerRelationships();

    @Query(
        "select algorithm from Algorithm algorithm left join fetch algorithm.placeholders left join fetch algorithm.parameters where algorithm.id =:id"
    )
    Optional<Algorithm> findOneWithEagerRelationships(@Param("id") Long id);
}
