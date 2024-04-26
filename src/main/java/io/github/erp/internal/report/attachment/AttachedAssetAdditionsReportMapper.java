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
