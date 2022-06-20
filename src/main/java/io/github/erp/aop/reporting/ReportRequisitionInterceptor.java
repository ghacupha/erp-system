package io.github.erp.aop.reporting;

import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.internal.report.ReportAssemblyService;
import io.github.erp.service.PdfReportRequisitionService;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import io.github.erp.service.dto.ReportRequisitionDTO;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Aspect for running report requests upon intercepting the response to the client
 */
@Aspect
public class ReportRequisitionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ReportRequisitionInterceptor.class);

    private final ReportAssemblyService<ReportRequisitionDTO> reportRequisitionService;

    public ReportRequisitionInterceptor(ReportAssemblyService<ReportRequisitionDTO> reportRequisitionService) {
        this.reportRequisitionService = reportRequisitionService;
    }

    @AfterReturning(
        pointcut="execution(* io.github.erp.erp.resources.ReportRequisitionResource.createReportRequisition(..))",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<ReportRequisitionDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        ReportRequisitionDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getReportId().toString();
        long entityId = reportDTO.getId();

        log.info("Report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        createReport(reportDTO);
    }

    @Async
    @SneakyThrows
    void updateReport(ReportRequisitionDTO report) {

        // TODO in the write step when the batch process is ready
        log.info("Updating report status for pdf report ID {}", report.getId());

        long start = System.currentTimeMillis();

//        CompletableFuture<PdfReportRequisitionDTO> reportAcquisition = CompletableFuture.supplyAsync(() -> pdfReportRequisitionService.findOne(report.getId()).get());

//        reportAcquisition.thenApplyAsync(rpt -> {
//            rpt.setReportStatus(ReportStatusTypes.SUCCESSFUL);
//            return pdfReportRequisitionService.partialUpdate(rpt);
//        });

//        reportAcquisition.thenApplyAsync(rpt -> {
//            log.info("Report status change complete for pdf report ID {} in {} milliseconds", rpt.getId(), System.currentTimeMillis() - start);
//            return rpt;
//        });
//
//        reportAcquisition.get();
    }

    @SneakyThrows
    @Async
    void createReport(ReportRequisitionDTO pdfReportRequisitionDTO) {

        // TODO split this into read step and process step when you setup a batch sequence for the report
        long start = System.currentTimeMillis();

        CompletableFuture<String> reportCreation = CompletableFuture.supplyAsync(() -> reportRequisitionService.createReport(pdfReportRequisitionDTO, ".pdf"));

        reportCreation.thenApplyAsync(reportPath -> {
            log.info("Report created successfully in {} milliseconds and set on the path {}", System.currentTimeMillis() - start, reportPath);
            return reportPath;
        });

        // reportCreation.get();
    }
}
