package io.github.erp.internal.report.service;

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
