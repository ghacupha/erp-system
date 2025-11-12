package io.github.erp.internal.report.attachment.models;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import org.springframework.stereotype.Component;

@Component
public class AttachedPrepaymentByAccountReportRequisitionMapper implements Mapping<PrepaymentByAccountReportRequisitionDTO, AttachedPrepaymentByAccountReportRequisitionDTO>  {

    @Override
    public PrepaymentByAccountReportRequisitionDTO toValue1(AttachedPrepaymentByAccountReportRequisitionDTO vs) {

        PrepaymentByAccountReportRequisitionDTO dto = new PrepaymentByAccountReportRequisitionDTO();
        dto.setId(vs.getId());
        dto.setRequestId(vs.getRequestId());
        dto.setTimeOfRequisition(vs.getTimeOfRequisition());
        dto.setFileChecksum(vs.getFileChecksum());
        dto.setFilename(vs.getFilename());
        dto.setReportParameters(vs.getReportParameters());
        dto.setReportFile(vs.getReportFile());
        dto.setReportFileContentType(vs.getReportFileContentType());
        dto.setReportDate(vs.getReportDate());
        dto.setTampered(vs.getTampered());
        dto.setRequestedBy(vs.getRequestedBy());
        dto.setLastAccessedBy(vs.getLastAccessedBy());

        return dto;
    }

    @Override
    public AttachedPrepaymentByAccountReportRequisitionDTO toValue2(PrepaymentByAccountReportRequisitionDTO vs) {

        AttachedPrepaymentByAccountReportRequisitionDTO attached = AttachedPrepaymentByAccountReportRequisitionDTO.builder()
            .id(vs.getId())
            .requestId(vs.getRequestId())
            .timeOfRequisition(vs.getTimeOfRequisition())
            .fileChecksum(vs.getFileChecksum())
            .filename(vs.getFilename())
            .reportParameters(vs.getReportParameters())
            .reportFile(vs.getReportFile())
            .reportFileContentType(vs.getReportFileContentType())
            .reportDate(vs.getReportDate())
            .tampered(vs.getTampered())
            .requestedBy(vs.getRequestedBy())
            .lastAccessedBy(vs.getLastAccessedBy())
            .build();

        return attached;
    }
}
