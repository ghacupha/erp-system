package io.github.erp.internal.repository;

import java.util.Optional;

import io.github.erp.repository.PrepaymentMappingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.github.erp.domain.PrepaymentMapping;

/**
 * Custom repo specifically for preppayment-mapping
 */
@Repository
public interface InternalPrepaymentMappingRepository extends
    PrepaymentMappingRepository,
    JpaRepository<PrepaymentMapping, Long>,
    JpaSpecificationExecutor<PrepaymentMapping> {

        Optional<PrepaymentMapping> findByParameterKeyEquals(String parameterKey);

}
