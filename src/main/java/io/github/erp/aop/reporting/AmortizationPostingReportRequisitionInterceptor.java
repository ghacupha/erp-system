package io.github.erp.aop.reporting;

import io.github.erp.internal.report.service.ExportReportService;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
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
 * Amortization posting requisition intercept intercepts the API call for the AmortizationPostingReportRequisition
 * routing the same to actual (but asynchronous) report-generating entities which will run a report
 * and export it to the file system.
 */
@Aspect
public class AmortizationPostingReportRequisitionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AmortizationPostingReportRequisitionInterceptor.class);

    private final ExportReportService<AmortizationPostingReportRequisitionDTO> amortizationPostingReportRequisitionExportReportService;

    public AmortizationPostingReportRequisitionInterceptor(
        ExportReportService<AmortizationPostingReportRequisitionDTO> amortizationPostingReportRequisitionExportReportService){
        this.amortizationPostingReportRequisitionExportReportService = amortizationPostingReportRequisitionExportReportService;
    }

    @AfterReturning(
        pointcut="reportRequisitionPointcut()",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<AmortizationPostingReportRequisitionDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        AmortizationPostingReportRequisitionDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getRequestId().toString();
        long entityId = reportDTO.getId();

        log.info("Report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        createReport(reportDTO);
    }

    @Async
    void createReport(AmortizationPostingReportRequisitionDTO reportDTO) {

        amortizationPostingReportRequisitionExportReportService.exportReport(reportDTO);
    }

    @Pointcut("execution(* io.github.erp.erp.resources.prepayments.AmortizationPostingReportRequisitionResourceProd.createAmortizationPostingReportRequisition(..))")
    public void reportRequisitionPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
}
