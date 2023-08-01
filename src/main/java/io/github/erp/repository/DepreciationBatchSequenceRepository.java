package io.github.erp.repository;

import io.github.erp.domain.DepreciationBatchSequence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DepreciationBatchSequence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepreciationBatchSequenceRepository
    extends JpaRepository<DepreciationBatchSequence, Long>, JpaSpecificationExecutor<DepreciationBatchSequence> {}
