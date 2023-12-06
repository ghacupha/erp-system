package io.github.erp.aop.reporting;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
        pointcut="execution(* io.github.erp.erp.resources.XlsxReportRequisitionResourceProd.createXlsxReportRequisition(..))",
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
