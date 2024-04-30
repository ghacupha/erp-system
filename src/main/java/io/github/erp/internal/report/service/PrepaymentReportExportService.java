package io.github.erp.internal.report.service;

import io.github.erp.domain.AssetAdditionsReport;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.AbstractCSVListExportService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.service.dto.AssetAdditionsReportDTO;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
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
public class PrepaymentReportExportService
    extends AbstractCSVListExportService<PrepaymentReportRequisitionDTO>
    implements ExportReportService<PrepaymentReportRequisitionDTO> {

    public PrepaymentReportExportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService) {
        super(reportsProperties, fileStorageService);
    }

    @Override
    public void exportReport(PrepaymentReportRequisitionDTO reportRequisition) {

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
    private Optional<List<AssetsAdditionsReportItemVM>> getEntries(PrepaymentReportRequisitionDTO reportRequisition) {
        return assetAdditionsReportRepository.findById(assetAdditionsReportDTO.getId())
            .map(report ->
                internalAssetAdditionsReportItemRepository.findAllByCapitalizationDate(report.getReportStartDate(), report.getReportEndDate(), Pageable.ofSize(Integer.MAX_VALUE))
                    .getContent())
            .map(assetAdditionsEntryInternalMapper::toValue2);
    }
}
