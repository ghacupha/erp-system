package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
