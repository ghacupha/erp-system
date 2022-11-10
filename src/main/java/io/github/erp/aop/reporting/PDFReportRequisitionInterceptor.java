package io.github.erp.aop.reporting;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.4-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.internal.report.assemblies.ReportAssemblyService;
import io.github.erp.service.PdfReportRequisitionService;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
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
 * Intercepts the response after a pdf-report-requisition is created and triggers the actual
 * report creation process
 */
@Aspect
public class PDFReportRequisitionInterceptor {
    private static final Logger log = LoggerFactory.getLogger(PDFReportRequisitionInterceptor.class);

    private final PdfReportRequisitionService pdfReportRequisitionService;
    private final ReportAssemblyService<PdfReportRequisitionDTO> reportRequisitionService;

    public PDFReportRequisitionInterceptor(
        ReportAssemblyService<PdfReportRequisitionDTO> reportRequisitionService,
        PdfReportRequisitionService pdfReportRequisitionService) {
        this.reportRequisitionService = reportRequisitionService;
        this.pdfReportRequisitionService = pdfReportRequisitionService;
    }

    @AfterReturning(
        pointcut="execution(* io.github.erp.erp.resources.PdfReportRequisitionResource.createPdfReportRequisition(..))",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<PdfReportRequisitionDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        PdfReportRequisitionDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getReportId().toString();
        long entityId = reportDTO.getId();

        log.info("PDF report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        createReport(reportDTO);

        pdfReportRequisitionService.findOne(reportDTO.getId()).ifPresent(this::updateReport);

    }

    @SneakyThrows
    @Async
    void createReport(PdfReportRequisitionDTO pdfReportRequisitionDTO) {

        long start = System.currentTimeMillis();

        CompletableFuture<String> reportCreation = CompletableFuture.supplyAsync(() -> reportRequisitionService.createReport(pdfReportRequisitionDTO, ".pdf"));

        reportCreation.thenApplyAsync(reportPath -> {
            log.info("Report created successfully in {} milliseconds and set on the path {}", System.currentTimeMillis() - start, reportPath);
            return reportPath;
        });

    }

    @Async
    @SneakyThrows
    void updateReport(PdfReportRequisitionDTO report) {

        log.info("Updating report status for pdf report ID {}", report.getId());

        long start = System.currentTimeMillis();

        CompletableFuture<PdfReportRequisitionDTO> reportAcquisition = CompletableFuture.supplyAsync(() -> pdfReportRequisitionService.findOne(report.getId()).get());

        reportAcquisition.thenApplyAsync(rpt -> {
            rpt.setReportStatus(ReportStatusTypes.SUCCESSFUL);
            return pdfReportRequisitionService.partialUpdate(rpt);
        });

        reportAcquisition.thenApplyAsync(rpt -> {
            log.info("Report status change complete for pdf report ID {} in {} milliseconds", rpt.getId(), System.currentTimeMillis() - start);
            return rpt;
        });

        reportAcquisition.get();
    }

}
