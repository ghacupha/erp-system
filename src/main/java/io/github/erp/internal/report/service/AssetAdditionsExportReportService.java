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
import io.github.erp.domain.AssetAdditionsReport;
import io.github.erp.domain.AssetAdditionsReportItemInternal;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.report.AbstractReportListCSVExportService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.repository.InternalAssetAdditionsReportItemRepository;
import io.github.erp.repository.AssetAdditionsReportRepository;
import io.github.erp.service.AssetAdditionsReportService;
import io.github.erp.service.dto.AssetAdditionsReportDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class AssetAdditionsExportReportService
    extends AbstractReportListCSVExportService<AssetsAdditionsReportItemVM>
    implements ExportReportService<AssetAdditionsReportDTO> {

        private final InternalAssetAdditionsReportItemRepository internalAssetAdditionsReportItemRepository;
        private final AssetAdditionsReportService assetAdditionsReportService;
        private final AssetAdditionsReportRepository assetAdditionsReportRepository;
        private final Mapping<AssetAdditionsReportItemInternal, AssetsAdditionsReportItemVM> assetAdditionsEntryInternalMapper;

    public AssetAdditionsExportReportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        InternalAssetAdditionsReportItemRepository internalAssetAdditionsReportItemRepository,
        AssetAdditionsReportService assetAdditionsReportService,
        AssetAdditionsReportRepository assetAdditionsReportRepository,
        Mapping<AssetAdditionsReportItemInternal, AssetsAdditionsReportItemVM> assetAdditionsEntryInternalMapper) {
            super(reportsProperties, fileStorageService);
        this.internalAssetAdditionsReportItemRepository = internalAssetAdditionsReportItemRepository;
        this.assetAdditionsReportService = assetAdditionsReportService;
        this.assetAdditionsReportRepository = assetAdditionsReportRepository;
        this.assetAdditionsEntryInternalMapper = assetAdditionsEntryInternalMapper;
    }

        @Override
        public void exportReport(AssetAdditionsReportDTO reportRequisition) {

            Optional<List<AssetsAdditionsReportItemVM>> reportListItems = getEntries(reportRequisition);

            reportListItems.ifPresent(reportList -> {

                try {
                    UUID fileName = UUID.randomUUID();

                    String fileChecksum = super.executeReport(reportList, fileName.toString());

                    reportRequisition.setFileChecksum(fileChecksum);
                    reportRequisition.setFilename(fileName);
                    reportRequisition.setReportParameters(getReportParameters(reportRequisition));

                    assetAdditionsReportService.save(reportRequisition);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        private String getReportParameters(AssetAdditionsReportDTO assetAdditionsReportDTO) {

            String parameters = "";

            Optional<AssetAdditionsReport> assetAdditionsReport = assetAdditionsReportRepository.findById(assetAdditionsReportDTO.getId());


            if(assetAdditionsReport.isPresent()) {

                parameters = "Start Date: ".concat(assetAdditionsReport.get().getReportStartDate().format(DateTimeFormatter.ISO_DATE).concat("; "));

                if (assetAdditionsReport.get().getReportEndDate() != null) {

                    parameters = parameters.concat("End Date: ".concat(assetAdditionsReport.get().getReportEndDate().format(DateTimeFormatter.ISO_DATE)).concat("; "));
                }
            }

            return parameters;
        }

        @NotNull
        private Optional<List<AssetsAdditionsReportItemVM>> getEntries(AssetAdditionsReportDTO assetAdditionsReportDTO) {
            return assetAdditionsReportRepository.findById(assetAdditionsReportDTO.getId())
                .map(report ->
                    internalAssetAdditionsReportItemRepository.findAllByCapitalizationDate(report.getReportStartDate(), report.getReportEndDate(), Pageable.ofSize(Integer.MAX_VALUE))
                        .getContent())
                .map(assetAdditionsEntryInternalMapper::toValue2);
        }

    @Override
    protected String getOutletCode() {
        return null;
    }

    @Override
    protected String getAssetCategoryName() {
        return null;
    }

    @Override
    protected String getPeriodCode() {
        return null;
    }
}
