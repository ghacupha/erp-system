package io.github.erp.repository;

import io.github.erp.domain.FixedAssetDepreciation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FixedAssetDepreciation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FixedAssetDepreciationRepository extends JpaRepository<FixedAssetDepreciation, Long>, JpaSpecificationExecutor<FixedAssetDepreciation> {
}
