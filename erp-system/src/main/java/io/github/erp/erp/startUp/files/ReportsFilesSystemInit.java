package io.github.erp.erp.startUp.files;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.service.wip.InternalWIPListReportService;
import io.github.erp.internal.service.wip.InternalWIPTransferListReportService;
import io.github.erp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service runs at startup to delete all reports in the reports directory and all
 * report metadata. This is to prevent overwhelming the fs with old transcient reports
 * that could be generated again on a whim.
 * This of course adds the burden of the startup sequence because at the same time we are also running
 * indices of certain searchable entities. At some time in future we would like to come back
 * and revisit this process with a stable batch sequence and quick gentle startups
 */
@Service
@Transactional
public class ReportsFilesSystemInit implements ApplicationListener<ApplicationReadyEvent> {

    private static Logger log = LoggerFactory.getLogger(ReportsFilesSystemInit.class);

    @Autowired
    @Qualifier("reportsFSStorageService")
    private FileStorageService storageService;

    @Autowired
    private ReportRequisitionService reportRequisitionService;

    @Autowired
    private PdfReportRequisitionService pdfReportRequisitionService;

    @Autowired
    private XlsxReportRequisitionService xlsxReportRequisitionService;

    @Autowired
    private ExcelReportExportService excelReportExportService;

    @Autowired
    private ReportStatusService reportStatusService;

    @Autowired
    private AutonomousReportService autonomousReportService;

    @Autowired
    private DepreciationReportService depreciationReportService;

    @Autowired
    private AssetAdditionsReportService assetAdditionsReportService;

    @Autowired
    private InternalWIPListReportService wipListReportService;

    @Autowired
    private InternalWIPTransferListReportService wipTransferListReportService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info("Initializing file-system for reports directory, and cleanup services. Standby...");

        // Delete all report entities from the system
        pdfReportRequisitionService.findAll(Pageable.unpaged())
            .forEach((report) -> pdfReportRequisitionService.delete(report.getId()));

        xlsxReportRequisitionService.findAll(Pageable.unpaged())
            .forEach(report -> xlsxReportRequisitionService.delete(report.getId()));

        reportRequisitionService.findAll(Pageable.unpaged())
            .forEach(report -> reportRequisitionService.delete(report.getId()));

        excelReportExportService.findAll(Pageable.unpaged())
            .forEach(report -> excelReportExportService.delete(report.getId()));

        reportStatusService.findAll(Pageable.unpaged())
            .forEach(report -> reportStatusService.delete(report.getId()));

        autonomousReportService.findAll(Pageable.unpaged())
            .forEach(report -> autonomousReportService.delete(report.getId()));

        depreciationReportService.findAll(Pageable.unpaged())
            .forEach(report -> depreciationReportService.delete(report.getId()));

        assetAdditionsReportService.findAll(Pageable.unpaged())
            .forEach(report -> assetAdditionsReportService.delete(report.getId()));

        wipListReportService.findAll(Pageable.unpaged())
            .forEach(report -> wipListReportService.delete(report.getId()));

        wipTransferListReportService.findAll(Pageable.unpaged())
            .forEach(report -> wipTransferListReportService.delete(report.getId()));

        log.info("All report metadata has been deleted, removing old files from the reports directory. Standby...");

        // delete the report files from the system
        storageService.deleteAll();

        // initialize storage
        storageService.init();

        log.info("File-system initialization sequence complete, reports directory is now restored and ready for instructions.");
    }
}
