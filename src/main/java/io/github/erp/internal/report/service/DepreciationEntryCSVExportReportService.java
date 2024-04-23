package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.domain.DepreciationReport;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.AbstractReportListCSVExportService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.repository.InternalDepreciationEntryItemRepository;
import io.github.erp.internal.repository.InternalDepreciationReportRepository;
import io.github.erp.repository.DepreciationPeriodRepository;
import io.github.erp.service.DepreciationReportService;
import io.github.erp.service.dto.DepreciationReportDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DepreciationEntryCSVExportReportService
    extends AbstractReportListCSVExportService<DepreciationEntryVM>
    implements DepreciationEntryExportReportService{

    private final DepreciationReportService depreciationReportService;
    private final InternalDepreciationReportRepository internalDepreciationReportRepository;
    private final DepreciationEntryInternalMapper depreciationEntryInternalMapper;
    private final DepreciationPeriodRepository depreciationPeriodRepository;
    private final InternalDepreciationEntryItemRepository internalDepreciationEntryRepository;

    private DepreciationReportDTO depreciationReportDTO;

    public DepreciationEntryCSVExportReportService(
            ReportsProperties reportsProperties,
            @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
            DepreciationReportService depreciationReportService, InternalDepreciationReportRepository internalDepreciationReportRepository,
            DepreciationEntryInternalMapper depreciationEntryInternalMapper,
            DepreciationPeriodRepository depreciationPeriodRepository,
            InternalDepreciationEntryItemRepository internalDepreciationEntryRepository) {
        super(reportsProperties, fileStorageService);
        this.depreciationReportService = depreciationReportService;
        this.internalDepreciationReportRepository = internalDepreciationReportRepository;
        this.depreciationEntryInternalMapper = depreciationEntryInternalMapper;
        this.depreciationPeriodRepository = depreciationPeriodRepository;
        this.internalDepreciationEntryRepository = internalDepreciationEntryRepository;
    }

    @Override
    public void exportDepreciationEntryReport(DepreciationReportDTO depreciationReportDTO) {

        this.depreciationReportDTO = depreciationReportDTO;

        Optional<List<DepreciationEntryVM>> depreciationEntryList = getEntries(depreciationReportDTO);

        depreciationEntryList.ifPresent(reportList -> {

            try {
                UUID fileName = UUID.randomUUID();

                String fileChecksum = super.executeReport(reportList, fileName.toString());

                depreciationReportDTO.setFileChecksum(fileChecksum);
                depreciationReportDTO.setFilename(fileName);
                depreciationReportDTO.setReportParameters(getReportParameters(depreciationReportDTO));

                depreciationReportService.save(depreciationReportDTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getReportParameters(DepreciationReportDTO depreciationReportDTO) {

        String parameters = "";

        Optional<DepreciationReport> depreciationReport = internalDepreciationReportRepository.findById(depreciationReportDTO.getId());


        if(depreciationReport.isPresent()) {

            parameters = "Period: ".concat(depreciationReport.get().getDepreciationPeriod().getPeriodCode().concat("; "));

            if (depreciationReport.get().getAssetCategory() != null) {

                parameters = parameters.concat("Category: ".concat(depreciationReport.get().getAssetCategory().getAssetCategoryName()).concat("; "));
            }

            if (depreciationReport.get().getServiceOutlet() != null) {

                parameters = parameters.concat("Outlet Code: ".concat(depreciationReport.get().getServiceOutlet().getOutletCode().concat("; ")));
            }
        }

        return parameters;
    }

    @NotNull
    private Optional<List<DepreciationEntryVM>> getEntries(DepreciationReportDTO depreciationReportDTO) {
        return depreciationPeriodRepository.findById(depreciationReportDTO.getDepreciationPeriod().getId())
            .map(period ->
                internalDepreciationEntryRepository.getDepreciationEntryByDepreciationPeriodEquals(period.getId(), Pageable.ofSize(Integer.MAX_VALUE))
                    .getContent())
            .map(depreciationEntryInternalMapper::toValue2);
    }

    @Override
    protected String getOutletCode() {
        return depreciationReportDTO.getServiceOutlet().getOutletCode();
    }

    @Override
    protected String getAssetCategoryName() {
        return depreciationReportDTO.getAssetCategory().getAssetCategoryName();
    }

    @Override
    protected String getPeriodCode() {
        return depreciationReportDTO.getDepreciationPeriod().getPeriodCode();
    }
}
