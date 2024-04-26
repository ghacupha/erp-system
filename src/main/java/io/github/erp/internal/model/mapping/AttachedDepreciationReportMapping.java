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
import io.github.erp.internal.report.attachment.AttachedDepreciationReportDTO;
import io.github.erp.service.dto.DepreciationReportDTO;
import org.springframework.stereotype.Component;

@Component
public class AttachedDepreciationReportMapping implements Mapping<DepreciationReportDTO, AttachedDepreciationReportDTO> {

    @Override
    public DepreciationReportDTO toValue1(AttachedDepreciationReportDTO vs) {
        DepreciationReportDTO depreciationReport = new DepreciationReportDTO();
        depreciationReport.setId(vs.getId());
        depreciationReport.setReportName(vs.getReportName());
        depreciationReport.setTimeOfReportRequest(vs.getTimeOfReportRequest());
        depreciationReport.setFileChecksum(vs.getFileChecksum());
        depreciationReport.setTampered(vs.getTampered());
        depreciationReport.setReportParameters(vs.getReportParameters());
        depreciationReport.setReportFile(vs.getReportFile());
        depreciationReport.setReportFileContentType(vs.getReportFileContentType());
        depreciationReport.setRequestedBy(vs.getRequestedBy());
        depreciationReport.setDepreciationPeriod(vs.getDepreciationPeriod());
        depreciationReport.setServiceOutlet(vs.getServiceOutlet());
        depreciationReport.setAssetCategory(vs.getAssetCategory());
        depreciationReport.setFilename(vs.getFilename());
        return depreciationReport;
    }

    @Override
    public AttachedDepreciationReportDTO toValue2(DepreciationReportDTO vs) {
        return AttachedDepreciationReportDTO.builder()
            .id(vs.getId())
            .reportName(vs.getReportName())
            .timeOfReportRequest(vs.getTimeOfReportRequest())
            .fileChecksum(vs.getFileChecksum())
            .tampered(vs.getTampered())
            .reportParameters(vs.getReportParameters())
            .filename(vs.getFilename())
            .reportFile(vs.getReportFile())
            .reportFileContentType(vs.getReportFileContentType())
            .requestedBy(vs.getRequestedBy())
            .depreciationPeriod(vs.getDepreciationPeriod())
            .serviceOutlet(vs.getServiceOutlet())
            .assetCategory(vs.getAssetCategory())
            .build();
    }
}
