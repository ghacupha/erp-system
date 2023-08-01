package io.github.erp.repository;

import io.github.erp.domain.DepreciationJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DepreciationJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepreciationJobRepository extends JpaRepository<DepreciationJob, Long>, JpaSpecificationExecutor<DepreciationJob> {}
