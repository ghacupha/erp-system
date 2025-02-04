package io.github.erp.internal.report.service;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.CSVListExportService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.wip.InternalWIPListItemService;
import io.github.erp.service.WIPListReportService;
import io.github.erp.service.dto.WIPListItemDTO;
import io.github.erp.service.dto.WIPListReportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Transactional
@Service("workInProgressReportRequisitionExportReportService")
public class WIPListReportRequisitionExportReportService
    extends CSVListExportService<WIPListItemDTO>
    implements ExportReportService<WIPListReportDTO>{

    private final WIPListReportService wipListReportService;
    private final InternalWIPListItemService wipListItemService;

    public WIPListReportRequisitionExportReportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        WIPListReportService wipListReportService, InternalWIPListItemService wipListItemService){
        super(reportsProperties, fileStorageService);
        this.wipListReportService = wipListReportService;
        this.wipListItemService = wipListItemService;
    }


    @Override
    public void exportReport(WIPListReportDTO reportRequisition) {

        Optional<List<WIPListItemDTO>> reportListItems = getEntries(reportRequisition);

        reportListItems.ifPresent(reportList -> {

            try {
                UUID fileName = UUID.randomUUID();

                String fileChecksum = super.executeReport(reportList, fileName.toString());

                reportRequisition.setFileChecksum(fileChecksum);
                reportRequisition.setFilename(fileName);
                reportRequisition.setReportParameters(getReportParameters(reportRequisition));

                wipListReportService.save(reportRequisition);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getReportParameters(WIPListReportDTO reportRequisition) {

        return "Time of Request: ".concat(reportRequisition.getTimeOfRequest().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    private Optional<List<WIPListItemDTO>> getEntries(WIPListReportDTO reportRequisition) {

        return Optional.of(
            wipListItemService.findAllReportItemsByReportDate(Pageable.ofSize(Integer.MAX_VALUE))
                .getContent());
    }
}
