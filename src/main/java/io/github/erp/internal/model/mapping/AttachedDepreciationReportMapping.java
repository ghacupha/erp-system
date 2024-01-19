package io.github.erp.internal.model.mapping;

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
