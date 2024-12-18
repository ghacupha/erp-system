package io.github.erp.internal.report.service;

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
import io.github.erp.domain.AssetAdditionsReport;
import io.github.erp.domain.AssetAdditionsReportItemInternal;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.report.AbstractAssetsReportListCSVExportService;
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
    extends AbstractAssetsReportListCSVExportService<AssetsAdditionsReportItemVM>
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
