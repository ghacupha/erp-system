package io.github.erp.repository;

import io.github.erp.domain.TaxReference;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TaxReference entity.
 */
@Repository
public interface TaxReferenceRepository extends JpaRepository<TaxReference, Long>, JpaSpecificationExecutor<TaxReference> {
    @Query(
        value = "select distinct taxReference from TaxReference taxReference left join fetch taxReference.placeholders",
        countQuery = "select count(distinct taxReference) from TaxReference taxReference"
    )
    Page<TaxReference> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct taxReference from TaxReference taxReference left join fetch taxReference.placeholders")
    List<TaxReference> findAllWithEagerRelationships();

    @Query("select taxReference from TaxReference taxReference left join fetch taxReference.placeholders where taxReference.id =:id")
    Optional<TaxReference> findOneWithEagerRelationships(@Param("id") Long id);
}
