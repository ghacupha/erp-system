package io.github.erp.repository;

import io.github.erp.domain.SystemModule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SystemModule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemModuleRepository extends JpaRepository<SystemModule, Long>, JpaSpecificationExecutor<SystemModule> {}
