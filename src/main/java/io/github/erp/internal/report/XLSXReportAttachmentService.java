package io.github.erp.internal.report;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.model.AttachedXlsxReportRequisitionDTO;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;


@Service
public class XLSXReportAttachmentService
    extends AbstractReportAttachmentService<XlsxReportRequisitionDTO>
    implements ReportAttachmentService<AttachedXlsxReportRequisitionDTO> {

    public XLSXReportAttachmentService(FileStorageService fileStorageService) {
        super(fileStorageService);
    }

    @SneakyThrows
    @Override
    public AttachedXlsxReportRequisitionDTO attachReport(AttachedXlsxReportRequisitionDTO one) {
        return (AttachedXlsxReportRequisitionDTO) super.attachReport(one);
    }

}
