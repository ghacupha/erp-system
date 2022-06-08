package io.github.erp.repository;

import io.github.erp.domain.UniversallyUniqueMapping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UniversallyUniqueMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniversallyUniqueMappingRepository extends JpaRepository<UniversallyUniqueMapping, Long> {}
