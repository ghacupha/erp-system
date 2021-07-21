package io.github.erp.repository;

import io.github.erp.domain.TaxReference;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TaxReference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxReferenceRepository extends JpaRepository<TaxReference, Long>, JpaSpecificationExecutor<TaxReference> {
}
