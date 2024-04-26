package io.github.erp.internal.model.mapping;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
