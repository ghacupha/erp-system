package io.github.erp.internal.report.attachment;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.AutonomousReportDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AutonomousReportAttachmentService
    extends AbstractReportAttachmentService<AutonomousReportDTO>
    implements ReportAttachmentService<AutonomousReportDTO> {

    private final Mapping<AutonomousReportDTO, AttachedAutonomousReportDTO> mapping;

    public AutonomousReportAttachmentService(
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
                                              Mapping<AutonomousReportDTO,
                                                  AttachedAutonomousReportDTO> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public AutonomousReportDTO attachReport(AutonomousReportDTO one) {
        one.setReportFileContentType("text/csv");
        return mapping.toValue1((AttachedAutonomousReportDTO) super.attachReport(mapping.toValue2(one), ".csv"));
    }
}
