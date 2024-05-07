package io.github.erp.internal.report.attachment;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.report.attachment.models.AttachedMonthlyPrepaymentReportRequisitionDTO;
import io.github.erp.service.dto.MonthlyPrepaymentReportRequisitionDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MonthlyPrepaymentReportRequisitionAttachmentService
    extends AbstractUnTamperedReportAttachmentService<MonthlyPrepaymentReportRequisitionDTO>
    implements ReportAttachmentService<MonthlyPrepaymentReportRequisitionDTO> {

    private final Mapping<MonthlyPrepaymentReportRequisitionDTO, AttachedMonthlyPrepaymentReportRequisitionDTO> mapping;

    public MonthlyPrepaymentReportRequisitionAttachmentService (
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        Mapping<MonthlyPrepaymentReportRequisitionDTO, AttachedMonthlyPrepaymentReportRequisitionDTO> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public MonthlyPrepaymentReportRequisitionDTO attachReport(MonthlyPrepaymentReportRequisitionDTO one) {
        one.setReportFileContentType("text/csv");
        return mapping.toValue1((AttachedMonthlyPrepaymentReportRequisitionDTO) super.attachReport(mapping.toValue2(one), ".csv"));
    }
}
