package io.github.erp.aop.reporting;

import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.internal.report.ReportAssemblyService;
import io.github.erp.service.ReportContentTypeService;
import io.github.erp.service.ReportRequisitionService;
import io.github.erp.service.dto.ReportContentTypeDTO;
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

/**
 * Aspect for running report requests upon intercepting the response to the client
 */
@Aspect
public class ReportRequisitionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ReportRequisitionInterceptor.class);

    private final ReportContentTypeService reportContentTypeService;
    private final ReportRequisitionService reportRequisitionService;
    private final ReportAssemblyService<ReportRequisitionDTO> reportAssemblyService;

    public ReportRequisitionInterceptor(ReportContentTypeService reportContentTypeService, ReportRequisitionService reportRequisitionService, ReportAssemblyService<ReportRequisitionDTO> reportAssemblyService) {
        this.reportContentTypeService = reportContentTypeService;
        this.reportRequisitionService = reportRequisitionService;
        this.reportAssemblyService = reportAssemblyService;
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

        updateReport(reportDTO);

    }

    @SneakyThrows
    @Async
    void createReport(ReportRequisitionDTO reportRequisitionDTO) {

        long start = System.currentTimeMillis();
        final ReportContentTypeDTO[] contentType = {null};

        reportContentTypeService.findOne(reportRequisitionDTO.getReportContentType().getId()).ifPresent(cType -> {
            contentType[0] = cType;
        });

        reportAssemblyService.createReport(reportRequisitionDTO, contentType[0].getReportFileExtension());


        log.info("Report created successfully in {} milliseconds and set on the path", System.currentTimeMillis() - start);

    }

    @Async
    @SneakyThrows
    void updateReport(ReportRequisitionDTO report) {

        reportRequisitionService.findOne(report.getId()).ifPresent(rptDTO -> {

            log.info("Updating report status for report ID {}", report.getId());

            long start = System.currentTimeMillis();

            reportRequisitionService.findOne(report.getId()).ifPresent(found -> {
                found.setReportStatus(ReportStatusTypes.SUCCESSFUL);
                reportRequisitionService.save(found);
                log.info("Report status change complete for pdf report ID {} in {} milliseconds", found.getId(), System.currentTimeMillis() - start);
            });

        });
    }
}
