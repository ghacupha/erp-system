package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.service.FiscalYearService;
import io.github.erp.service.dto.*;
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
 * Export service for the monthly-prepayment-report
 */
@Slf4j
@Transactional
@Service("monthlyPrepaymentReportUserInitiatedExportService")
public class MonthlyPrepaymentReportUserInitiatedExportService
    extends CSVListExportService<MonthlyPrepaymentOutstandingReportItemDTO>
    implements ExportReportService<MonthlyPrepaymentReportRequisitionDTO> {

    private final FiscalYearService fiscalYearService;
    private final InternalMonthlyPrepaymentReportRequisitionService internalMonthlyPrepaymentReportRequisitionService;
    private final InternalMonthlyPrepaymentOutstandingReportItemService internalMonthlyPrepaymentOutstandingReportItemService;

    public MonthlyPrepaymentReportUserInitiatedExportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        FiscalYearService fiscalYearService,
        InternalMonthlyPrepaymentReportRequisitionService internalMonthlyPrepaymentReportRequisitionService,
        InternalMonthlyPrepaymentOutstandingReportItemService internalMonthlyPrepaymentOutstandingReportItemService){

        super(reportsProperties, fileStorageService);

        this.fiscalYearService = fiscalYearService;
        this.internalMonthlyPrepaymentReportRequisitionService = internalMonthlyPrepaymentReportRequisitionService;
        this.internalMonthlyPrepaymentOutstandingReportItemService = internalMonthlyPrepaymentOutstandingReportItemService;
    }

    @Override
    public void exportReport(MonthlyPrepaymentReportRequisitionDTO reportRequisition) {

        Optional<List<MonthlyPrepaymentOutstandingReportItemDTO>> reportListItems = getEntries(reportRequisition);

        reportListItems.ifPresent(reportList -> {

            try {
                UUID fileName = UUID.randomUUID();

                String fileChecksum = super.executeReport(reportList, fileName.toString());

                reportRequisition.setFileChecksum(fileChecksum);
                reportRequisition.setFilename(fileName);
                reportRequisition.setReportParameters(getReportParameters(reportRequisition));

                internalMonthlyPrepaymentReportRequisitionService.save(reportRequisition);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getReportParameters(MonthlyPrepaymentReportRequisitionDTO reportRequisition) {

        return "Fiscal. Year: ".concat(reportRequisition.getFiscalYear().getFiscalYearCode());
    }

    private Optional<List<MonthlyPrepaymentOutstandingReportItemDTO>> getEntries(MonthlyPrepaymentReportRequisitionDTO reportRequisition) {

        AtomicReference<Optional<List<MonthlyPrepaymentOutstandingReportItemDTO>>> monthlyPrepaymentOutstandingReportItemDTOList = new AtomicReference<>(Optional.empty());

        fiscalYearService.findOne(reportRequisition.getFiscalYear().getId()).ifPresent(fiscalYearDTO -> {
            monthlyPrepaymentOutstandingReportItemDTOList.set(internalMonthlyPrepaymentOutstandingReportItemService.findReportItemsByFiscalYear(fiscalYearDTO));
        });

        return monthlyPrepaymentOutstandingReportItemDTOList.get();
    }
}
