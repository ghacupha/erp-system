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
import io.github.erp.domain.ReportStatus;
import io.github.erp.internal.repository.InternalProcessStatusRepository;
import io.github.erp.internal.repository.InternalReportStatusRepository;
import io.github.erp.internal.report.assemblies.ReportAssemblyService;
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
        pointcut="execution(* io.github.erp.erp.resources.reports.ExcelReportExportResourceProd.createExcelReportExport(..))",
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
