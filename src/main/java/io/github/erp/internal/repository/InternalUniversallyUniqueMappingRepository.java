package io.github.erp.internal.repository;

import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.UniversallyUniqueMappingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InternalUniversallyUniqueMappingRepository extends
    UniversallyUniqueMappingRepository,
    JpaRepository<UniversallyUniqueMapping, Long>,
    JpaSpecificationExecutor<UniversallyUniqueMapping> {


    Optional<UniversallyUniqueMapping> findByUniversalKeyEquals(String key);
}
