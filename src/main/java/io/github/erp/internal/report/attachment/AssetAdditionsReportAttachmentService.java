package io.github.erp.internal.report.attachment;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.AssetAdditionsReportDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AssetAdditionsReportAttachmentService
    extends AbstractUnTamperedReportAttachmentService<AssetAdditionsReportDTO>
    implements ReportAttachmentService<AssetAdditionsReportDTO> {

    private final Mapping<AssetAdditionsReportDTO, AttachedAssetAdditionsReportDTO> mapping;

    public AssetAdditionsReportAttachmentService (
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        Mapping<AssetAdditionsReportDTO, AttachedAssetAdditionsReportDTO> mapping) {
        super(fileStorageService);
        this.mapping = mapping;
    }

    @SneakyThrows
    @Override
    public AssetAdditionsReportDTO attachReport(AssetAdditionsReportDTO one) {
        one.setReportFileContentType("text/csv");
        return mapping.toValue1((AttachedAssetAdditionsReportDTO) super.attachReport(mapping.toValue2(one), ".csv"));
    }


}
