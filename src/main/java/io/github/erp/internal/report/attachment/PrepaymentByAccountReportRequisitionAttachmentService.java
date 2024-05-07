package io.github.erp.internal.report.attachment;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.report.attachment.models.AttachedPrepaymentByAccountReportRequisitionDTO;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Attachment service for the prepayment-by-account report
 */
@Transactional(readOnly = true)
@Service
public class PrepaymentByAccountReportRequisitionAttachmentService
    extends AbstractUnTamperedReportAttachmentService<PrepaymentByAccountReportRequisitionDTO>
    implements ReportAttachmentService<PrepaymentByAccountReportRequisitionDTO> {


    private final Mapping<PrepaymentByAccountReportRequisitionDTO, AttachedPrepaymentByAccountReportRequisitionDTO> mapping;

    public PrepaymentByAccountReportRequisitionAttachmentService (
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        Mapping<PrepaymentByAccountReportRequisitionDTO, AttachedPrepaymentByAccountReportRequisitionDTO> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public PrepaymentByAccountReportRequisitionDTO attachReport(PrepaymentByAccountReportRequisitionDTO one) {
        one.setReportFileContentType("text/csv");
        return mapping.toValue1((AttachedPrepaymentByAccountReportRequisitionDTO) super.attachReport(mapping.toValue2(one), ".csv"));
    }
}
