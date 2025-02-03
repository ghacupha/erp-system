package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.AttachedWIPListReportDTO;
import io.github.erp.service.dto.WIPListReportDTO;
import org.springframework.stereotype.Component;

@Component
public class AttachedWIPListReportMapping implements Mapping<WIPListReportDTO, AttachedWIPListReportDTO> {

    @Override
    public WIPListReportDTO toValue1(AttachedWIPListReportDTO vs) {

        WIPListReportDTO dto = new WIPListReportDTO();
        dto.setId(vs.getId());
        dto.setTimeOfRequest(vs.getTimeOfRequest());
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
    public AttachedWIPListReportDTO toValue2(WIPListReportDTO vs) {
        return AttachedWIPListReportDTO.builder()
            .id(vs.getId())
            .timeOfRequest(vs.getTimeOfRequest())
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
