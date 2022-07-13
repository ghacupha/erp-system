package io.github.erp.internal.report.attachment;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.model.AttachedExcelReportExportDTO;
import io.github.erp.internal.model.mapping.AttachedExcelReportExportDTOMapping;
import io.github.erp.service.dto.ExcelReportExportDTO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * ReportAttachmentService for the excelReportExport entity
 */
@Service
public class ExcelReportExportAttachmentService
    extends AbstractReportAttachmentService<ExcelReportExportDTO>
    implements ReportAttachmentService<ExcelReportExportDTO> {

    private final AttachedExcelReportExportDTOMapping mapping;

    public ExcelReportExportAttachmentService(FileStorageService fileStorageService, AttachedExcelReportExportDTOMapping mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public ExcelReportExportDTO attachReport(ExcelReportExportDTO one) {
        return mapping.toValue1((AttachedExcelReportExportDTO) super.attachReport(mapping.toValue2(one), ".xlsx"));
    }
}
