package io.github.erp.internal.report.attachment;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.NbvReportDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NBVReportAttachmentService
    extends AbstractUnTamperedReportAttachmentService<NbvReportDTO>
    implements ReportAttachmentService<NbvReportDTO> {

    private final Mapping<NbvReportDTO, AttachedNBVReportDTO> mapping;

    public NBVReportAttachmentService (
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        Mapping<NbvReportDTO, AttachedNBVReportDTO> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public NbvReportDTO attachReport(NbvReportDTO one) {
        one.setReportFileContentType("text/csv");
        return mapping.toValue1((AttachedNBVReportDTO) super.attachReport(mapping.toValue2(one), ".csv"));
    }
}
