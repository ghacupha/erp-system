package io.github.erp.internal.report.repository;

import io.github.erp.domain.ProcessStatus;
import io.github.erp.repository.ProcessStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternalProcessStatusRepository extends ProcessStatusRepository, JpaRepository<ProcessStatus, Long>, JpaSpecificationExecutor<ProcessStatus> {

    /**
     * This query returns the status where the description is equivalent to the description provided
     *
     * @param desc
     * @return
     */
    Optional<ProcessStatus> findProcessStatusByDescriptionEquals(String desc);
}
