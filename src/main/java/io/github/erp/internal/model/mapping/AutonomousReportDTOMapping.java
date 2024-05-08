package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
        dto.setFileChecksum(vs.getFileChecksum());
        dto.setReportTampered(vs.getReportTampered());

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
            .fileCheckSum(vs.getFileChecksum())
            .reportTampered(vs.getReportTampered())
            .build();
    }
}
