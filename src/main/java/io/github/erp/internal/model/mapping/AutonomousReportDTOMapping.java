package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.report.attachment.AttachedAutonomousReportDTO;
import io.github.erp.service.dto.AutonomousReportDTO;
import org.springframework.stereotype.Component;

@Component
public class AutonomousReportDTOMapping implements Mapping<AutonomousReportDTO, AttachedAutonomousReportDTO> {

    @Override
    public AutonomousReportDTO toValue1(AttachedAutonomousReportDTO vs) {

        if ( vs == null ) {
            return null;
        }

        AutonomousReportDTO dto = new AutonomousReportDTO();

        dto.setId(vs.getId());
        dto.setReportName(vs.getReportName());
        dto.setReportParameters(vs.getReportParameters());
        dto.setCreatedAt(vs.getCreatedAt());
        dto.setReportFilename(vs.getReportFilename());
        dto.setReportFile(vs.getReportFile());
        dto.setReportFileContentType(vs.getReportFileContentType());
        dto.setReportMappings(vs.getReportMappings());
        dto.setPlaceholders(vs.getPlaceholders());
        dto.setCreatedBy(vs.getCreatedBy());


        return dto;
    }

    @Override
    public AttachedAutonomousReportDTO toValue2(AutonomousReportDTO vs) {

        if ( vs == null ) {
            return null;
        }


        return AttachedAutonomousReportDTO.builder()
            .id(vs.getId())
            .reportName(vs.getReportName())
            .reportParameters(vs.getReportParameters())
            .createdAt(vs.getCreatedAt())
            .reportFilename(vs.getReportFilename())
            .reportFile(vs.getReportFile())
            .reportFileContentType(vs.getReportFileContentType())
            .reportMappings(vs.getReportMappings())
            .placeholders(vs.getPlaceholders())
            .createdBy(vs.getCreatedBy())
            // TODO .fileCheckSum(vs.fileChecksum)
            .build();
    }
}
