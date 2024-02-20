package io.github.erp.internal.report.attachment;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.NbvReportDTO;
import org.springframework.stereotype.Component;

@Component
public class NBVReportAttachmentMapper implements Mapping<NbvReportDTO, AttachedNBVReportDTO> {

    @Override
    public NbvReportDTO toValue1(AttachedNBVReportDTO vs) {
        NbvReportDTO dto = new NbvReportDTO();
        dto.setId(vs.getId());
        dto.setReportName(vs.getReportName());
        dto.setTimeOfReportRequest(vs.getTimeOfReportRequest());
        dto.setFileChecksum(vs.getFileChecksum());
        dto.setTampered(vs.getTampered());
        dto.setFilename(vs.getFilename());
        dto.setReportParameters(vs.getReportParameters());
        dto.setReportFile(vs.getReportFile());
        dto.setReportFileContentType(vs.getReportFileContentType());
        dto.setRequestedBy(vs.getRequestedBy());
        dto.setDepreciationPeriod(vs.getDepreciationPeriod());
        dto.setServiceOutlet(vs.getServiceOutlet());
        dto.setAssetCategory(vs.getAssetCategory());



        return dto;
    }

    @Override
    public AttachedNBVReportDTO toValue2(NbvReportDTO vs) {

        return AttachedNBVReportDTO
            .builder()
            .id(vs.getId())
            .reportName(vs.getReportName())
            .timeOfReportRequest(vs.getTimeOfReportRequest())
            .fileChecksum(vs.getFileChecksum())
            .tampered(vs.getTampered())
            .filename(vs.getFilename())
            .reportParameters(vs.getReportParameters())
            .reportFile(vs.getReportFile())
            .reportFileContentType(vs.getReportFileContentType())
            .requestedBy(vs.getRequestedBy())
            .depreciationPeriod(vs.getDepreciationPeriod())
            .serviceOutlet(vs.getServiceOutlet())
            .assetCategory(vs.getAssetCategory())
            .build();
    }
}
