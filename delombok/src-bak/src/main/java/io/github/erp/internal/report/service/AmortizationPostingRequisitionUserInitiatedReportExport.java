package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.internal.service.prepayments.*;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Execution of the amortization-posting-report on the file system as a csv file
 */
@Slf4j
@Transactional
@Service("amortizationPostingRequisitionUserInitiatedReportExport")
public class AmortizationPostingRequisitionUserInitiatedReportExport
    extends CSVListExportService<AmortizationPostingReportDTO>
    implements ExportReportService<AmortizationPostingReportRequisitionDTO>{

    private final InternalAmortizationPeriodService internalAmortizationPeriodService;
    private final InternalAmortizationPostingReportService internalAmortizationPostingReportService;
    private final InternalAmortizationPostingReportRequisitionService internalAmortizationPostingReportRequisitionService;

    public AmortizationPostingRequisitionUserInitiatedReportExport(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        InternalAmortizationPeriodService internalAmortizationPeriodService, InternalAmortizationPostingReportService internalAmortizationPostingReportService,
        InternalAmortizationPostingReportRequisitionService internalAmortizationPostingReportRequisitionService) {
        super(reportsProperties, fileStorageService);
        this.internalAmortizationPeriodService = internalAmortizationPeriodService;

        this.internalAmortizationPostingReportService = internalAmortizationPostingReportService;
        this.internalAmortizationPostingReportRequisitionService = internalAmortizationPostingReportRequisitionService;
    }

    @Override
    public void exportReport(AmortizationPostingReportRequisitionDTO reportRequisition) {

        Optional<List<AmortizationPostingReportDTO>> reportListItems = getEntries(reportRequisition);

        reportListItems.ifPresent(reportList -> {

            try {
                UUID fileName = UUID.randomUUID();

                String fileChecksum = super.executeReport(reportList, fileName.toString());

                reportRequisition.setFileChecksum(fileChecksum);
                reportRequisition.setFilename(fileName);
                reportRequisition.setReportParameters(getReportParameters(reportRequisition));

                internalAmortizationPostingReportRequisitionService.save(reportRequisition);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getReportParameters(AmortizationPostingReportRequisitionDTO reportRequisition) {

        return "Amort. Period: ".concat(reportRequisition.getAmortizationPeriod().getPeriodCode());
    }

    private Optional<List<AmortizationPostingReportDTO>> getEntries(AmortizationPostingReportRequisitionDTO reportRequisition) {

        AtomicReference<Optional<List<AmortizationPostingReportDTO>>> amortizationPostingItems = new AtomicReference<>(Optional.empty());

        internalAmortizationPeriodService.findOne(reportRequisition.getAmortizationPeriod().getId()).ifPresent(
            amortizationPeriod -> {
                amortizationPostingItems.set(internalAmortizationPostingReportService.findAllByReportDate(amortizationPeriod.getEndDate()));

                log.debug("Posting report created with {} entries", amortizationPostingItems.get().get().size());
            });
        return amortizationPostingItems.get();
    }
}
