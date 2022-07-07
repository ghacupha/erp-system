package io.github.erp.internal.report.repository;

import io.github.erp.domain.ReportStatus;
import io.github.erp.repository.ReportStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InternalReportStatusRepository extends ReportStatusRepository, JpaRepository<ReportStatus, Long>, JpaSpecificationExecutor<ReportStatus> {

    Optional<ReportStatus> findReportStatusByReportIdEquals(UUID reportId);
}
