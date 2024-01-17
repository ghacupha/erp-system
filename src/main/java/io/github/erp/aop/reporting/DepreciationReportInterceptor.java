package io.github.erp.aop.reporting;

import io.github.erp.internal.report.service.DepreciationEntryExportReportService;
import io.github.erp.service.dto.DepreciationReportDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@Aspect
public class DepreciationReportInterceptor {


    private static final Logger log = LoggerFactory.getLogger(DepreciationReportInterceptor.class);

    private final DepreciationEntryExportReportService depreciationEntryExportReportService;

    public DepreciationReportInterceptor(DepreciationEntryExportReportService depreciationEntryExportReportService) {
        this.depreciationEntryExportReportService = depreciationEntryExportReportService;
    }

    @AfterReturning(
        pointcut="execution(* io.github.erp.erp.resources.depreciation.DepreciationReportResourceProd.createDepreciationReport(..))",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<DepreciationReportDTO> response) {

        log.info("Depreciation report requisition response intercept completed successfully");

        DepreciationReportDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getReportName();
        long entityId = reportDTO.getId();

        log.info("Depreciation report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        depreciationEntryExportReportService.exportDepreciationEntryReport(reportDTO);
    }

}
