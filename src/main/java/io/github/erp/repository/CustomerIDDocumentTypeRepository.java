package io.github.erp.repository;

import io.github.erp.domain.CustomerIDDocumentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CustomerIDDocumentType entity.
 */
@Repository
public interface CustomerIDDocumentTypeRepository
    extends JpaRepository<CustomerIDDocumentType, Long>, JpaSpecificationExecutor<CustomerIDDocumentType> {
    @Query(
        value = "select distinct customerIDDocumentType from CustomerIDDocumentType customerIDDocumentType left join fetch customerIDDocumentType.placeholders",
        countQuery = "select count(distinct customerIDDocumentType) from CustomerIDDocumentType customerIDDocumentType"
    )
    Page<CustomerIDDocumentType> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct customerIDDocumentType from CustomerIDDocumentType customerIDDocumentType left join fetch customerIDDocumentType.placeholders"
    )
    List<CustomerIDDocumentType> findAllWithEagerRelationships();

    @Query(
        "select customerIDDocumentType from CustomerIDDocumentType customerIDDocumentType left join fetch customerIDDocumentType.placeholders where customerIDDocumentType.id =:id"
    )
    Optional<CustomerIDDocumentType> findOneWithEagerRelationships(@Param("id") Long id);
}
