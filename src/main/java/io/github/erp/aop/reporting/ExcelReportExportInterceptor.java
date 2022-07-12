package io.github.erp.aop.reporting;

/*-
 * Erp System - Mark II No 17 (Baruch Series)
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

import io.github.erp.domain.ReportStatus;
import io.github.erp.internal.report.repository.InternalProcessStatusRepository;
import io.github.erp.internal.report.repository.InternalReportStatusRepository;
import io.github.erp.internal.report.ReportAssemblyService;
import io.github.erp.service.ExcelReportExportService;
import io.github.erp.service.dto.ExcelReportExportDTO;
import io.github.erp.service.dto.ReportStatusDTO;
import io.github.erp.service.mapper.ReportStatusMapper;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Intercepts requests on the excel-report-export resource to provide the actual intended
 * functionality of creating a report from a template obtained from the report-design entity
 */
@Aspect
public class ExcelReportExportInterceptor {

    private final static Logger log = LoggerFactory.getLogger(ExcelReportExportInterceptor.class);

    private final ReportStatusMapper reportStatusMapper;
    private final InternalProcessStatusRepository processStatusRepository;
    private final InternalReportStatusRepository reportStatusRepository;
    private final ExcelReportExportService reportRequisitionService;
    private final ReportAssemblyService<ExcelReportExportDTO> reportAssemblyService;

    public ExcelReportExportInterceptor(ReportStatusMapper reportStatusMapper, InternalProcessStatusRepository processStatusRepository, InternalReportStatusRepository reportStatusRepository, ExcelReportExportService reportRequisitionService, ReportAssemblyService<ExcelReportExportDTO> reportAssemblyService) {
        this.reportStatusMapper = reportStatusMapper;
        this.processStatusRepository = processStatusRepository;
        this.reportStatusRepository = reportStatusRepository;
        this.reportRequisitionService = reportRequisitionService;
        this.reportAssemblyService = reportAssemblyService;
    }

    @AfterReturning(
        pointcut="execution(* io.github.erp.erp.resources.ExcelReportExportResource.createExcelReportExport(..))",
        returning="response")
    public void getCreatedReportInfo(JoinPoint joinPoint, ResponseEntity<ExcelReportExportDTO> response) {

        log.info("Report requisition response intercept completed successfully");

        ExcelReportExportDTO reportDTO = Objects.requireNonNull(response.getBody());
        String reportId = reportDTO.getReportId().toString();
        long entityId = reportDTO.getId();

        log.info("Report requisition with id: {} has been registered, with entity id # {} commencing report creation sequence...", reportId, entityId);

        reportDTO.setReportStatus(reportStatusMapper.toDto(acceptReportStatus(reportDTO)));

        createReport(reportDTO);

        reportRequisitionService.findOne(reportDTO.getId()).ifPresent(this::updateReport);

    }

    @SneakyThrows
    @Async
    void createReport(ExcelReportExportDTO reportRequisitionDTO) {

        long start = System.currentTimeMillis();

        String reportPath = reportAssemblyService.createReport(reportRequisitionDTO, ".xlsx");

        log.info("Report created successfully in {} milliseconds and set on the path {}", System.currentTimeMillis() - start, reportPath);
    }

    @Async
    void updateReport(ExcelReportExportDTO report) {

        log.info("Updating report status for xlsx report ID {}", report.getId());

        long start = System.currentTimeMillis();

        reportRequisitionService.findOne(report.getId()).ifPresent(found -> {
            ReportStatusDTO rs = null;

            try {
                rs = reportStatusMapper.toDto(reportStatusRepository.save(createSuccessReportStatus(found)));
            } catch (Exception e) {
                throw new RuntimeException("Exception encountered. Refer to reporting manual.", e);
            }

            found.setReportStatus(rs);
            reportRequisitionService.save(found);
            log.info("Report status change complete for xlsx report ID {} in {} milliseconds", found.getId(), System.currentTimeMillis() - start);
        });
    }

    private ReportStatus acceptReportStatus(ExcelReportExportDTO excelExport) {
        ReportStatus rs = new ReportStatus();
        rs.setReportId(excelExport.getReportId());
        rs.setReportName(excelExport.getReportName());

        processStatusRepository.findProcessStatusByDescriptionEquals("ACCEPTED")
            .ifPresentOrElse(rs::setProcessStatus,
                () -> {throw new RuntimeException("Process status code for ACCEPTED not found. Please refer to process state setup");});

        return reportStatusRepository.save(rs);
    }

    private ReportStatus createSuccessReportStatus(ExcelReportExportDTO found) {
        AtomicReference<ReportStatus> stats = new AtomicReference<>();
        reportStatusRepository.findReportStatusByReportIdEquals(found.getReportId()).ifPresentOrElse(reportStat -> {
            processStatusRepository.findProcessStatusByDescriptionEquals("SUCCESSFUL")
                .ifPresentOrElse(reportStat::setProcessStatus,
                    () -> {throw new RuntimeException("Process status code for SUCCESSFUL not found. Please refer to process state setup");
                });
            stats.set(reportStat);
        }, () -> {throw new RuntimeException("");});
        return stats.get();
    }
}
