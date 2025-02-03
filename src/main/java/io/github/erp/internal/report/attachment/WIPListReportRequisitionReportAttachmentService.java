package io.github.erp.internal.report.attachment;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.AttachedWIPListReportDTO;
import io.github.erp.service.dto.WIPListReportDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("wipListReportRequisitionReportAttachmentService")
public class WIPListReportRequisitionReportAttachmentService
    extends AbstractUnTamperedReportAttachmentService<WIPListReportDTO>
    implements ReportAttachmentService<WIPListReportDTO>{

    private final Mapping<WIPListReportDTO, AttachedWIPListReportDTO> mapping;

    public WIPListReportRequisitionReportAttachmentService (
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        Mapping<WIPListReportDTO, AttachedWIPListReportDTO> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public WIPListReportDTO attachReport(WIPListReportDTO one) {
        one.setReportFileContentType("text/csv");
        return mapping.toValue1((AttachedWIPListReportDTO) super.attachReport(mapping.toValue2(one), ".csv"));
    }
}
