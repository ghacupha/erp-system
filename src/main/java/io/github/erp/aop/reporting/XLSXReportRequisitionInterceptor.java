package io.github.erp.aop.reporting;

import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.internal.report.ReportAssemblyService;
import io.github.erp.service.XlsxReportRequisitionService;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
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

@Aspect
public class XLSXReportRequisitionInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ReportRequisitionInterceptor.class);

    private final XlsxReportRequisitionService reportRequisitionService;
    private final ReportAssemblyService<XlsxReportRequisitionDTO> reportAssemblyService;

    public XLSXReportRequisitionInterceptor(XlsxReportRequisitionService reportRequisitionService, ReportAssemblyService<XlsxReportRequisitionDTO> reportAssemblyService) {
        this.reportRequisitionService = reportRequisitionService;
        this.reportAssemblyService = reportAssemblyService;
    }

    @AfterReturning(
        pointcut="execution(* io.github.erp.erp.resources.XlsxReportRequisitionResource.createXlsxReportRequisition(..))",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<XlsxReportRequisitionDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        XlsxReportRequisitionDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getReportId().toString();
        long entityId = reportDTO.getId();

        log.info("Report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        createReport(reportDTO);

        reportRequisitionService.findOne(reportDTO.getId()).ifPresent(this::updateReport);

    }

    @SneakyThrows
    @Async
    void createReport(XlsxReportRequisitionDTO reportRequisitionDTO) {

        long start = System.currentTimeMillis();

        CompletableFuture<String> reportCreation = CompletableFuture.supplyAsync(() -> reportAssemblyService.createReport(reportRequisitionDTO, ".xlsx"));

        reportCreation.thenApplyAsync(reportPath -> {
            log.info("Report created successfully in {} milliseconds and set on the path {}", System.currentTimeMillis() - start, reportPath);
            return reportPath;
        });

    }

    @Async
    @SneakyThrows
    void updateReport(XlsxReportRequisitionDTO report) {

        log.info("Updating report status for pdf report ID {}", report.getId());

        long start = System.currentTimeMillis();

        CompletableFuture<XlsxReportRequisitionDTO> reportAcquisition = CompletableFuture.supplyAsync(() -> reportRequisitionService.findOne(report.getId()).get());

        reportAcquisition.thenApplyAsync(rpt -> {
            rpt.setReportStatus(ReportStatusTypes.SUCCESSFUL);
            return reportRequisitionService.partialUpdate(rpt);
        });

        reportAcquisition.thenApplyAsync(rpt -> {
            log.info("Report status change complete for pdf report ID {} in {} milliseconds", rpt.getId(), System.currentTimeMillis() - start);
            return rpt;
        });

        reportAcquisition.get();
    }
}
