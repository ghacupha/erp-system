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
import io.github.erp.domain.NetBookValueEntryInternal;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.report.AbstractReportListCSVExportService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.repository.InternalNetBookValueEntryRepository;
import io.github.erp.internal.service.applicationUser.CurrentUserContext;
import io.github.erp.repository.DepreciationPeriodRepository;
import io.github.erp.repository.NbvReportRepository;
import io.github.erp.service.NbvReportService;
import io.github.erp.service.dto.NbvReportDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
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

    private final ApplicationUserMapper applicationUserMapper;
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
        ApplicationUserMapper applicationUserMapper, DepreciationPeriodRepository depreciationPeriodRepository,
        InternalNetBookValueEntryRepository internalNetBookValueEntryRepository,
        Mapping<NetBookValueEntryInternal, NetBookValueEntryVM> netBookValueEntryVMMapping) {
        super(reportsProperties, fileStorageService);
        this.nbvReportService = nbvReportService;
        this.applicationUserMapper = applicationUserMapper;
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
                nbvReportDTO.setRequestedBy(applicationUserMapper.toDto(CurrentUserContext.getCurrentUser()));

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
                internalNetBookValueEntryRepository
                    .getNBVEntryByDepreciationPeriod(period.getId(), Pageable.ofSize(Integer.MAX_VALUE))
                    .getContent())
            .map(netBookValueEntryVMMapping::toValue2);
    }

    @Override
    protected String getOutletCode() {
        if (reportDTO != null && reportDTO.getAssetCategory() != null && reportDTO.getAssetCategory().getAssetCategoryName() != null) {
            return reportDTO.getServiceOutlet().getOutletCode();
        } else {
            return "All Outlets";
        }
    }

    @Override
    protected String getAssetCategoryName() {
        if (reportDTO != null && reportDTO.getAssetCategory() != null && reportDTO.getAssetCategory().getAssetCategoryName() != null) {
            return reportDTO.getAssetCategory().getAssetCategoryName();
        } else {
            return "All Categories";
        }
    }


    @Override
    protected String getPeriodCode() {
        return reportDTO.getDepreciationPeriod().getPeriodCode() == null ? "" : reportDTO.getDepreciationPeriod().getPeriodCode();
    }
}
