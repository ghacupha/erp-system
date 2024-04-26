package io.github.erp.aop.reporting;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.internal.report.assemblies.ReportAssemblyService;
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

/**
 * Designed to intercept the xlsx-report-requisition resource when creating a report. As the
 * response is returning to the user the report-service is invoked in the background to continue
 * with the work of assembling the report.
 */
@Aspect
public class XLSXReportRequisitionInterceptor {
    private static final Logger log = LoggerFactory.getLogger(XLSXReportRequisitionInterceptor.class);

    private final XlsxReportRequisitionService reportRequisitionService;
    private final ReportAssemblyService<XlsxReportRequisitionDTO> reportAssemblyService;

    public XLSXReportRequisitionInterceptor(ReportAssemblyService<XlsxReportRequisitionDTO> reportAssemblyService, XlsxReportRequisitionService reportRequisitionService) {
        this.reportRequisitionService = reportRequisitionService;
        this.reportAssemblyService = reportAssemblyService;
    }

    @AfterReturning(
        pointcut="execution(* io.github.erp.erp.resources.reports.XlsxReportRequisitionResourceProd.createXlsxReportRequisition(..))",
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

        String reportPath = reportAssemblyService.createReport(reportRequisitionDTO, ".xlsx");

        log.info("Report created successfully in {} milliseconds and set on the path {}", System.currentTimeMillis() - start, reportPath);
    }

    @Async
    @SneakyThrows
    void updateReport(XlsxReportRequisitionDTO report) {

        log.info("Updating report status for xlsx report ID {}", report.getId());

        long start = System.currentTimeMillis();

        reportRequisitionService.findOne(report.getId()).ifPresent(found -> {
            found.setReportStatus(ReportStatusTypes.SUCCESSFUL);
            reportRequisitionService.save(found);
            log.info("Report status change complete for xlsx report ID {} in {} milliseconds", found.getId(), System.currentTimeMillis() - start);
        });
    }
}
