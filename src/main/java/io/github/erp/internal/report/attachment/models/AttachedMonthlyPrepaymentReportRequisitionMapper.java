package io.github.erp.internal.report.attachment.models;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.MonthlyPrepaymentReportRequisitionDTO;
import org.springframework.stereotype.Component;

@Component
public class AttachedMonthlyPrepaymentReportRequisitionMapper implements Mapping<MonthlyPrepaymentReportRequisitionDTO, AttachedMonthlyPrepaymentReportRequisitionDTO> {

    @Override
    public MonthlyPrepaymentReportRequisitionDTO toValue1(AttachedMonthlyPrepaymentReportRequisitionDTO vs) {

        MonthlyPrepaymentReportRequisitionDTO dto = new MonthlyPrepaymentReportRequisitionDTO();
        dto.setId(vs.getId());
        dto.setReportName(vs.getReportName());
        dto.setTimeOfRequisition(vs.getTimeOfRequisition());
        dto.setFileChecksum(vs.getFileChecksum());
        dto.setTampered(vs.getTampered());
        dto.setFilename(vs.getFilename());
        dto.setReportParameters(vs.getReportParameters());
        dto.setReportFile(vs.getReportFile());
        dto.setReportFileContentType(vs.getReportFileContentType());
        dto.setRequestedBy(vs.getRequestedBy());
        dto.setLastAccessedBy(vs.getLastAccessedBy());
        dto.setFiscalYear(vs.getFiscalYear());

        return dto;
    }

    @Override
    public AttachedMonthlyPrepaymentReportRequisitionDTO toValue2(MonthlyPrepaymentReportRequisitionDTO vs) {

        AttachedMonthlyPrepaymentReportRequisitionDTO dto = AttachedMonthlyPrepaymentReportRequisitionDTO.builder()
            .id(vs.getId())
            .reportName(vs.getReportName())
            .timeOfRequisition(vs.getTimeOfRequisition())
            .fileChecksum(vs.getFileChecksum())
            .tampered(vs.getTampered())
            .filename(vs.getFilename())
            .reportParameters(vs.getReportParameters())
            .reportFile(vs.getReportFile())
            .reportFileContentType(vs.getReportFileContentType())
            .requestedBy(vs.getRequestedBy())
            .lastAccessedBy(vs.getLastAccessedBy())
            .fiscalYear(vs.getFiscalYear())
            .build();

        return dto;
    }
}
