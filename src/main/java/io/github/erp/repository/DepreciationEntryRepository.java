package io.github.erp.repository;

import io.github.erp.domain.DepreciationEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DepreciationEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepreciationEntryRepository extends JpaRepository<DepreciationEntry, Long>, JpaSpecificationExecutor<DepreciationEntry> {}
