package io.github.erp.repository;

import io.github.erp.domain.BusinessDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusinessDocument entity.
 */
@Repository
public interface BusinessDocumentRepository extends JpaRepository<BusinessDocument, Long>, JpaSpecificationExecutor<BusinessDocument> {
    @Query(
        value = "select distinct businessDocument from BusinessDocument businessDocument left join fetch businessDocument.applicationMappings left join fetch businessDocument.placeholders",
        countQuery = "select count(distinct businessDocument) from BusinessDocument businessDocument"
    )
    Page<BusinessDocument> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct businessDocument from BusinessDocument businessDocument left join fetch businessDocument.applicationMappings left join fetch businessDocument.placeholders"
    )
    List<BusinessDocument> findAllWithEagerRelationships();

    @Query(
        "select businessDocument from BusinessDocument businessDocument left join fetch businessDocument.applicationMappings left join fetch businessDocument.placeholders where businessDocument.id =:id"
    )
    Optional<BusinessDocument> findOneWithEagerRelationships(@Param("id") Long id);
}
