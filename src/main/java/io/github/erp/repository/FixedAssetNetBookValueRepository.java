package io.github.erp.repository;

import io.github.erp.domain.FixedAssetNetBookValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FixedAssetNetBookValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FixedAssetNetBookValueRepository
    extends JpaRepository<FixedAssetNetBookValue, Long>, JpaSpecificationExecutor<FixedAssetNetBookValue> {}
