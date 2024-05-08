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
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.report.dynamic.ReportGenerator;
import io.github.erp.internal.report.dynamic.ZipUtility;
import io.github.erp.internal.repository.InternalDepreciationEntryItemRepository;
import io.github.erp.repository.DepreciationPeriodRepository;
import io.github.erp.service.dto.DepreciationReportDTO;
import net.sf.dynamicreports.report.exception.DRException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Deprecated
@Transactional
public class DepreciationEntryExportReportServiceImpl implements DepreciationEntryExportReportService {

    private static final String SYSTEM_PASSWORD = "testPass1"; // TODO Inject own password

    private final DepreciationEntryInternalMapper depreciationEntryInternalMapper;
    private final DepreciationPeriodRepository depreciationPeriodRepository;
    private final InternalDepreciationEntryItemRepository internalDepreciationEntryRepository;
    private final FileStorageService fileStorageService;

    public DepreciationEntryExportReportServiceImpl(
            DepreciationPeriodRepository depreciationPeriodRepository,
            InternalDepreciationEntryItemRepository internalDepreciationEntryRepository,
            @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
            ReportsProperties reportsProperties, DepreciationEntryInternalMapper depreciationEntryInternalMapper) {
        this.depreciationPeriodRepository = depreciationPeriodRepository;
        this.internalDepreciationEntryRepository = internalDepreciationEntryRepository;
        this.fileStorageService = fileStorageService;
        // SYSTEM_PASSWORD = reportsProperties.getReportPassword();
        this.depreciationEntryInternalMapper = depreciationEntryInternalMapper;
    }

    @Async
    public void exportDepreciationEntryReport(DepreciationReportDTO depreciationReportDTO) {

        Optional<List<DepreciationEntryVM>> depreciationEntryList = getEntries(depreciationReportDTO);

        depreciationEntryList.ifPresent(reportList -> {

            // byte[] zippedStream =  exportDepreciationEntryList(reportList, depreciationReportDTO.getReportName());

            byte[] zippedStream =  exportDepreciationEntryListUnencrypted(reportList, UUID.randomUUID().toString() + ".xlsx", depreciationReportDTO.getReportName());

            // TODO export file to fileSystem
            fileStorageService.save(zippedStream, depreciationReportDTO.getReportName()+".zip");
        });
    }

    private byte[] exportDepreciationEntryList(List<DepreciationEntryVM> entryList, String excelFileName) {

        byte[] reportStream = new byte[0];
        try {
            reportStream = ReportGenerator.generateExcelReport(entryList, DepreciationEntryVM.class, SYSTEM_PASSWORD);
        } catch (DRException e) {
            e.printStackTrace();
        }

        return ZipUtility.zipByteStream(reportStream, excelFileName, SYSTEM_PASSWORD.toCharArray());
    }

    private byte[] exportDepreciationEntryListUnencrypted(List<DepreciationEntryVM> entryList, String excelFileName, String reportTitle) {

        byte[] reportStream = new byte[0];
        try {
            reportStream = ReportGenerator.generateUnencryptedExcelReport(entryList, DepreciationEntryVM.class, reportTitle);
        } catch (DRException e) {
            e.printStackTrace();
        }

        return ZipUtility.unencryptedZipByteStream(reportStream, excelFileName);
    }

    @NotNull
    private Optional<List<DepreciationEntryVM>> getEntries(DepreciationReportDTO depreciationReportDTO) {
        return depreciationPeriodRepository.findById(depreciationReportDTO.getDepreciationPeriod().getId())
            .map(period ->
                internalDepreciationEntryRepository.getDepreciationEntryByDepreciationPeriodEquals(period.getId(), Pageable.ofSize(Integer.MAX_VALUE))
        .getContent())
            .map(depreciationEntryInternalMapper::toValue2);
    }
}
