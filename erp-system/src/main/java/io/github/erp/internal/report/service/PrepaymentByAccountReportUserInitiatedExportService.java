package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.internal.service.prepayments.InternalPrepaymentAccountReportService;
import io.github.erp.internal.service.prepayments.InternalPrepaymentByAccountReportRequisitionService;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Transactional
@Service("prepaymentByAccountReportUserInitiatedExportService")
public class PrepaymentByAccountReportUserInitiatedExportService
    extends CSVListExportService<PrepaymentAccountReportDTO>
    implements ExportReportService<PrepaymentByAccountReportRequisitionDTO> {

    private final InternalPrepaymentAccountReportService internalPrepaymentAccountReportService;
    private final InternalPrepaymentByAccountReportRequisitionService internalPrepaymentByAccountReportRequisitionService;


    public PrepaymentByAccountReportUserInitiatedExportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        InternalPrepaymentAccountReportService internalPrepaymentAccountReportService,
        InternalPrepaymentByAccountReportRequisitionService internalPrepaymentByAccountReportRequisitionService){
        super(reportsProperties, fileStorageService);

        this.internalPrepaymentAccountReportService = internalPrepaymentAccountReportService;
        this.internalPrepaymentByAccountReportRequisitionService = internalPrepaymentByAccountReportRequisitionService;
    }


    @Override
    public void exportReport(PrepaymentByAccountReportRequisitionDTO reportRequisition) {

        Optional<List<PrepaymentAccountReportDTO>> reportListItems = getEntries(reportRequisition);

        reportListItems.ifPresent(reportList -> {

            try {
                UUID fileName = UUID.randomUUID();

                String fileChecksum = super.executeReport(reportList, fileName.toString());

                reportRequisition.setFileChecksum(fileChecksum);
                reportRequisition.setFilename(fileName);
                reportRequisition.setReportParameters(getReportParameters(reportRequisition));

                internalPrepaymentByAccountReportRequisitionService.save(reportRequisition);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getReportParameters(PrepaymentByAccountReportRequisitionDTO reportRequisition) {

        return "Report Date: ".concat(reportRequisition.getReportDate().format(DateTimeFormatter.ISO_DATE));
    }

    private Optional<List<PrepaymentAccountReportDTO>> getEntries(PrepaymentByAccountReportRequisitionDTO reportRequisition) {

        return Optional.of(internalPrepaymentAccountReportService.findAllByReportDate(reportRequisition.getReportDate(), Pageable.ofSize(Integer.MAX_VALUE))
            .toList());
    }
}
