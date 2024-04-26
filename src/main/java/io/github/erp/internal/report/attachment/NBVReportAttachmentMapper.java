package io.github.erp.internal.report.attachment;

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
