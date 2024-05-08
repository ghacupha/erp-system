package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.internal.report.CSVListExportService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.prepayments.InternalPrepaymentReportRequisitionService;
import io.github.erp.internal.service.prepayments.InternalPrepaymentReportService;
import io.github.erp.service.dto.PrepaymentReportDTO;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This service exports a list prepayment-report items into the file system in the
 * reports directory as a csv file
 */
@Transactional
@Service("prepaymentReportUserInitiatedExportService")
public class PrepaymentReportUserInitiatedExportService
    extends CSVListExportService<PrepaymentReportDTO>
    implements ExportReportService<PrepaymentReportRequisitionDTO> {

    private final InternalPrepaymentReportService internalPrepaymentReportService;
    private final InternalPrepaymentReportRequisitionService internalPrepaymentReportRequisitionService;

    public PrepaymentReportUserInitiatedExportService(
        ReportsProperties reportsProperties,
        InternalPrepaymentReportService internalPrepaymentReportService,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService, InternalPrepaymentReportRequisitionService internalPrepaymentReportRequisitionService) {
        super(reportsProperties, fileStorageService);
        this.internalPrepaymentReportService = internalPrepaymentReportService;
        this.internalPrepaymentReportRequisitionService = internalPrepaymentReportRequisitionService;
    }

    @Override
    public void exportReport(PrepaymentReportRequisitionDTO reportRequisition) {

        Optional<List<PrepaymentReportDTO>> reportListItems = getEntries(reportRequisition);

        reportListItems.ifPresent(reportList -> {

            try {
                UUID fileName = UUID.randomUUID();

                String fileChecksum = super.executeReport(reportList, fileName.toString());

                reportRequisition.setFileChecksum(fileChecksum);
                reportRequisition.setFilename(fileName);
                reportRequisition.setReportParameters(getReportParameters(reportRequisition));

                internalPrepaymentReportRequisitionService.save(reportRequisition);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getReportParameters(PrepaymentReportRequisitionDTO reportRequisition) {

        return "Report Date: ".concat(reportRequisition.getReportDate().format(DateTimeFormatter.ISO_DATE));
    }

    private Optional<List<PrepaymentReportDTO>> getEntries(PrepaymentReportRequisitionDTO reportRequisition) {

        return internalPrepaymentReportService.getReportListByReportDate(reportRequisition.getReportDate());
    }
}
