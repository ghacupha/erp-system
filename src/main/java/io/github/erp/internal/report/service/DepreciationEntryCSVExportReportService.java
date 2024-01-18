package io.github.erp.internal.report.service;

import io.github.erp.domain.DepreciationReport;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.AbstractReportListCSVExportService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.repository.InternalDepreciationEntryRepository;
import io.github.erp.internal.repository.InternalDepreciationReportRepository;
import io.github.erp.repository.DepreciationPeriodRepository;
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

    private final InternalDepreciationReportRepository internalDepreciationReportRepository;
    private final DepreciationEntryInternalMapper depreciationEntryInternalMapper;
    private final DepreciationPeriodRepository depreciationPeriodRepository;
    private final InternalDepreciationEntryRepository internalDepreciationEntryRepository;

    public DepreciationEntryCSVExportReportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        InternalDepreciationReportRepository internalDepreciationReportRepository,
        DepreciationEntryInternalMapper depreciationEntryInternalMapper,
        DepreciationPeriodRepository depreciationPeriodRepository,
        InternalDepreciationEntryRepository internalDepreciationEntryRepository) {
        super(reportsProperties, fileStorageService);
        this.internalDepreciationReportRepository = internalDepreciationReportRepository;
        this.depreciationEntryInternalMapper = depreciationEntryInternalMapper;
        this.depreciationPeriodRepository = depreciationPeriodRepository;
        this.internalDepreciationEntryRepository = internalDepreciationEntryRepository;
    }

    @Override
    public void exportDepreciationEntryReport(DepreciationReportDTO depreciationReportDTO) {

        Optional<List<DepreciationEntryVM>> depreciationEntryList = getEntries(depreciationReportDTO);

        depreciationEntryList.ifPresent(reportList -> {

            try {
                String fileName = UUID.randomUUID().toString();

                String fileChecksum = super.executeReport(reportList, fileName);

                // todo depreciationReportDTO.setFileChecksum(fileChecksum);
                // todo depreciationReportDTO.setFileName(fileName);
                // todo depreciationReportDTO.setReportParameters(getReportParameters(depreciationReportDTO));

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
}
