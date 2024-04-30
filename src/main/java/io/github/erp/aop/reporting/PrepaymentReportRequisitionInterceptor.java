package io.github.erp.aop.reporting;

import io.github.erp.internal.report.service.ExportReportService;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;

/**
 * Intercepts prepayment-report-requisition, and triggers a report-generation process using the
 * ExportReportService interface
 */
@Aspect
public class PrepaymentReportRequisitionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PrepaymentReportRequisitionInterceptor.class);

    private final ExportReportService<PrepaymentReportRequisitionDTO> prepaymentReportExportReportService;

    public PrepaymentReportRequisitionInterceptor(ExportReportService<PrepaymentReportRequisitionDTO> prepaymentReportExportReportService) {
        this.prepaymentReportExportReportService = prepaymentReportExportReportService;
    }

    @AfterReturning(
        pointcut="prepaymentReportRequisitionPointcut()",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<PrepaymentReportRequisitionDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        PrepaymentReportRequisitionDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getReportName().toString();
        long entityId = reportDTO.getId();

        log.info("Report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        createReport(reportDTO);
    }

    @Async
    void createReport(PrepaymentReportRequisitionDTO reportDTO) {
        prepaymentReportExportReportService.exportReport(reportDTO);
    }

    @Pointcut("execution(* io.github.erp.erp.resources.prepayments.PrepaymentReportRequisitionResourceProd.createPrepaymentReportRequisition(..))")
    public void prepaymentReportRequisitionPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
