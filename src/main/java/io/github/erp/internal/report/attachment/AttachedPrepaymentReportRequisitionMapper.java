package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import org.springframework.stereotype.Component;

@Component
public class AttachedPrepaymentReportRequisitionMapper implements Mapping<PrepaymentReportRequisitionDTO, AttachedPrepaymentReportRequisitionDTO> {

    @Override
    public PrepaymentReportRequisitionDTO toValue1(AttachedPrepaymentReportRequisitionDTO vs) {

        PrepaymentReportRequisitionDTO dto = new PrepaymentReportRequisitionDTO();
        dto.setId(vs.getId());
        dto.setReportName(vs.getReportName());
        dto.setReportDate(vs.getReportDate());
        dto.setTimeOfRequisition(vs.getTimeOfRequisition());
        dto.setFileChecksum(vs.getFileChecksum());
        dto.setTampered(vs.getTampered());
        dto.setFilename(vs.getFilename());
        dto.setReportParameters(vs.getReportParameters());
        dto.setReportFile(vs.getReportFile());
        dto.setReportFileContentType(vs.getReportFileContentType());
        dto.setRequestedBy(vs.getRequestedBy());
        dto.setLastAccessedBy(vs.getLastAccessedBy());
        return dto;
    }

    @Override
    public AttachedPrepaymentReportRequisitionDTO toValue2(PrepaymentReportRequisitionDTO vs) {
        return AttachedPrepaymentReportRequisitionDTO.builder()
            .id(vs.getId())
            .reportName(vs.getReportName())
            .reportDate(vs.getReportDate())
            .timeOfRequisition(vs.getTimeOfRequisition())
            .fileChecksum(vs.getFileChecksum())
            .tampered(vs.getTampered())
            .filename(vs.getFilename())
            .reportParameters(vs.getReportParameters())
            .reportFile(vs.getReportFile())
            .reportFileContentType(vs.getReportFileContentType())
            .requestedBy(vs.getRequestedBy())
            .lastAccessedBy(vs.getLastAccessedBy())
            .build();
    }
}
