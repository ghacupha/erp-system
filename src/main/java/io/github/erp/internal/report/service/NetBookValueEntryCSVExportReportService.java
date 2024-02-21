package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.domain.NetBookValueEntryInternal;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.report.AbstractReportListCSVExportService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.repository.InternalNetBookValueEntryRepository;
import io.github.erp.repository.DepreciationPeriodRepository;
import io.github.erp.repository.NbvReportRepository;
import io.github.erp.service.NbvReportService;
import io.github.erp.service.dto.NbvReportDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This service creates a report and saves the report on the file system
 */
@Service
@Transactional
public class NetBookValueEntryCSVExportReportService
    extends AbstractReportListCSVExportService<NetBookValueEntryVM>
    implements NetBookValueEntryExportReportService<NbvReportDTO> {

    private final NbvReportService nbvReportService;
    private final DepreciationPeriodRepository depreciationPeriodRepository;
    private final InternalNetBookValueEntryRepository internalNetBookValueEntryRepository;
    private final Mapping<NetBookValueEntryInternal, NetBookValueEntryVM> netBookValueEntryVMMapping;

    private NbvReportDTO reportDTO;

    public NetBookValueEntryCSVExportReportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        NbvReportService nbvReportService,
        NbvReportRepository nbvReportRepository,
        DepreciationPeriodRepository depreciationPeriodRepository,
        InternalNetBookValueEntryRepository internalNetBookValueEntryRepository,
        Mapping<NetBookValueEntryInternal, NetBookValueEntryVM> netBookValueEntryVMMapping) {
        super(reportsProperties, fileStorageService);
        this.nbvReportService = nbvReportService;
        this.depreciationPeriodRepository = depreciationPeriodRepository;
        this.internalNetBookValueEntryRepository = internalNetBookValueEntryRepository;
        this.netBookValueEntryVMMapping = netBookValueEntryVMMapping;
    }

    @Override
    public void exportEntryReport(NbvReportDTO nbvReportDTO) {

        this.reportDTO = nbvReportDTO;

        Optional<List<NetBookValueEntryVM>> entryList = getEntries(nbvReportDTO);

        entryList.ifPresent(reportList -> {

            try {
                UUID fileName = UUID.randomUUID();

                String fileChecksum = super.executeReport(reportList, fileName.toString());

                nbvReportDTO.setFileChecksum(fileChecksum);
                nbvReportDTO.setFilename(fileName);
                nbvReportDTO.setReportParameters(super.getReportParameters());

                nbvReportService.save(nbvReportDTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @NotNull
    private Optional<List<NetBookValueEntryVM>> getEntries(NbvReportDTO nbvReportDTO) {

        return depreciationPeriodRepository.findById(nbvReportDTO.getDepreciationPeriod().getId())
            .map(period ->
                internalNetBookValueEntryRepository.getNBVEntryByDepreciationPeriodEqual(period.getId(), Pageable.ofSize(Integer.MAX_VALUE))
                    .getContent())
            .map(netBookValueEntryVMMapping::toValue2);
    }

    @Override
    protected String getOutletCode() {
        return reportDTO.getServiceOutlet().getOutletCode();
    }

    @Override
    protected String getAssetCategoryName() {
        return reportDTO.getAssetCategory().getAssetCategoryName();
    }

    @Override
    protected String getPeriodCode() {
        return reportDTO.getDepreciationPeriod().getPeriodCode();
    }
}
