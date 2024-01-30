package io.github.erp.internal.report.attachment;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.AssetAdditionsReportDTO;
import org.springframework.stereotype.Component;

@Component
public class AttachedAssetAdditionsReportMapper implements Mapping<AssetAdditionsReportDTO, AttachedAssetAdditionsReportDTO> {

    @Override
    public AssetAdditionsReportDTO toValue1(AttachedAssetAdditionsReportDTO vs) {
        AssetAdditionsReportDTO dto = new AssetAdditionsReportDTO();

        dto.setId(vs.getId());
        dto.setTimeOfRequest(vs.getTimeOfRequest());
        dto.setReportStartDate(vs.getReportStartDate());
        dto.setReportEndDate(vs.getReportEndDate());
        dto.setRequestId(vs.getRequestId());
        dto.setFileChecksum(vs.getFileChecksum());
        dto.setTampered(vs.getTampered());
        dto.setFilename(vs.getFilename());
        dto.setReportParameters(vs.getReportParameters());
        dto.setReportFile(vs.getReportFile());
        dto.setReportFileContentType(vs.getReportFileContentType());
        dto.setRequestedBy(vs.getRequestedBy());

        return dto;
    }

    @Override
    public AttachedAssetAdditionsReportDTO toValue2(AssetAdditionsReportDTO vs) {

        return AttachedAssetAdditionsReportDTO.builder()
            .id(vs.getId())
            .timeOfRequest(vs.getTimeOfRequest())
            .reportStartDate(vs.getReportStartDate())
            .reportEndDate(vs.getReportEndDate())
            .requestId(vs.getRequestId())
            .fileChecksum(vs.getFileChecksum())
            .tampered(vs.getTampered())
            .filename(vs.getFilename())
            .reportParameters(vs.getReportParameters())
            .reportFile(vs.getReportFile())
            .reportFileContentType(vs.getReportFileContentType())
            .requestedBy(vs.getRequestedBy())
            .build();
    }
}
