package io.github.erp.internal.report.repository;

import io.github.erp.domain.ReportDesign;
import io.github.erp.repository.ReportDesignRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InternalReportDesignRepository extends ReportDesignRepository, JpaRepository<ReportDesign, Long>, JpaSpecificationExecutor<ReportDesign> {

    Optional<ReportDesign> findReportDesignByIdEquals(long id);
}
