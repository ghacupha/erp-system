package io.github.erp.internal.report.attachment;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AmortizationPostingReportRequisitionAttachmentService
    extends AbstractUnTamperedReportAttachmentService<AmortizationPostingReportRequisitionDTO>
    implements ReportAttachmentService<AmortizationPostingReportRequisitionDTO> {

    private final Mapping<AmortizationPostingReportRequisitionDTO, AttachedAmortizationPostingReportRequisition> mapping;


    public AmortizationPostingReportRequisitionAttachmentService (
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        Mapping<AmortizationPostingReportRequisitionDTO, AttachedAmortizationPostingReportRequisition> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public AmortizationPostingReportRequisitionDTO attachReport(AmortizationPostingReportRequisitionDTO one) {
        one.setReportFileContentType("text/csv");
        return mapping.toValue1((AttachedAmortizationPostingReportRequisition) super.attachReport(mapping.toValue2(one), ".csv"));
    }
}
