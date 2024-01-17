package io.github.erp.internal.report.service;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.report.dynamic.ReportGenerator;
import io.github.erp.internal.report.dynamic.ZipUtility;
import io.github.erp.internal.repository.InternalDepreciationEntryRepository;
import io.github.erp.repository.DepreciationPeriodRepository;
import io.github.erp.service.dto.DepreciationReportDTO;
import net.sf.dynamicreports.report.exception.DRException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepreciationEntryExportReportServiceImpl implements DepreciationEntryExportReportService {

    private final String SYSTEM_PASSWORD; // TODO Inject own password

    private final DepreciationEntryInternalMapper depreciationEntryInternalMapper;
    private final DepreciationEntryVMMapper depreciationEntryVMMapper;
    private final DepreciationPeriodRepository depreciationPeriodRepository;
    private final InternalDepreciationEntryRepository internalDepreciationEntryRepository;
    private final FileStorageService fileStorageService;

    public DepreciationEntryExportReportServiceImpl(
        DepreciationEntryVMMapper depreciationEntryVMMapper,
        DepreciationPeriodRepository depreciationPeriodRepository,
        InternalDepreciationEntryRepository internalDepreciationEntryRepository,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        ReportsProperties reportsProperties, DepreciationEntryInternalMapper depreciationEntryInternalMapper) {
        this.depreciationEntryVMMapper = depreciationEntryVMMapper;
        this.depreciationPeriodRepository = depreciationPeriodRepository;
        this.internalDepreciationEntryRepository = internalDepreciationEntryRepository;
        this.fileStorageService = fileStorageService;
        SYSTEM_PASSWORD = reportsProperties.getReportPassword();
        this.depreciationEntryInternalMapper = depreciationEntryInternalMapper;
    }

    @Async
    public void exportDepreciationEntryReport(DepreciationReportDTO depreciationReportDTO) {

        Optional<List<DepreciationEntryVM>> depreciationEntryList = getEntries(depreciationReportDTO);

        depreciationEntryList.ifPresent(reportList -> {

            byte[] zippedStream =  exportDepreciationEntryList(reportList);

            // TODO export file to fileSystem
            fileStorageService.save(zippedStream, depreciationReportDTO.getReportName()+".zip");
        });
    }

    private byte[] exportDepreciationEntryList(List<DepreciationEntryVM> entryList) {

        byte[] reportStream = new byte[0];
        try {
            reportStream = ReportGenerator.generateExcelReport(entryList, DepreciationEntryVM.class, SYSTEM_PASSWORD);
        } catch (DRException e) {
            e.printStackTrace();
        }

        return ZipUtility.zipByteStream(reportStream, SYSTEM_PASSWORD.toCharArray());
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
