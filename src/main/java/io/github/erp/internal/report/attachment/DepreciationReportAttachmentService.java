package io.github.erp.internal.report.attachment;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.AutonomousReportDTO;
import io.github.erp.service.dto.DepreciationReportDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepreciationReportAttachmentService
    extends AbstractUnTamperedReportAttachmentService<DepreciationReportDTO>
    implements ReportAttachmentService<DepreciationReportDTO> {


    private final Mapping<DepreciationReportDTO, AttachedDepreciationReportDTO> mapping;

    public DepreciationReportAttachmentService (
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        Mapping<DepreciationReportDTO,
            AttachedDepreciationReportDTO> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public DepreciationReportDTO attachReport(DepreciationReportDTO one) {
        one.setReportFileContentType("text/csv");
        return mapping.toValue1((AttachedDepreciationReportDTO) super.attachReport(mapping.toValue2(one), ".csv"));
    }

}
