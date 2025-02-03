package io.github.erp.aop.reporting.wip;

import io.github.erp.aop.reporting.amortizationPosting.AmortizationPostingReportRequisitionInterceptor;
import io.github.erp.internal.report.service.ExportReportService;
import io.github.erp.service.dto.WIPListReportDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;

@Aspect
public class WIPListReportRequisitionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AmortizationPostingReportRequisitionInterceptor.class);

    private final ExportReportService<WIPListReportDTO> exportReportService;

    public WIPListReportRequisitionInterceptor(ExportReportService<WIPListReportDTO> exportReportService) {
        this.exportReportService = exportReportService;
    }

    @AfterReturning(
        pointcut="reportRequisitionPointcut()",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, @NotNull ResponseEntity<WIPListReportDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        WIPListReportDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getRequestId().toString();
        long entityId = reportDTO.getId();

        log.info("Report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        createReport(reportDTO);
    }

    @Async
    void createReport(WIPListReportDTO reportDTO) {

        exportReportService.exportReport(reportDTO);
    }

    @Pointcut("execution(* io.github.erp.erp.resources.wip.WIPListReportResourceProd.createWIPListReport(..))")
    public void reportRequisitionPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}

