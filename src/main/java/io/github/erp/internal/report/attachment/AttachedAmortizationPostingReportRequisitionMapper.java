package io.github.erp.internal.report.attachment;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
import org.springframework.stereotype.Component;

@Component
public class AttachedAmortizationPostingReportRequisitionMapper implements Mapping<AmortizationPostingReportRequisitionDTO, AttachedAmortizationPostingReportRequisition> {


    @Override
    public AmortizationPostingReportRequisitionDTO toValue1(AttachedAmortizationPostingReportRequisition vs) {

        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisition = new AmortizationPostingReportRequisitionDTO();

        amortizationPostingReportRequisition.setId(vs.getId());
        amortizationPostingReportRequisition.setRequestId(vs.getRequestId());
        amortizationPostingReportRequisition.setTimeOfRequisition(vs.getTimeOfRequisition());
        amortizationPostingReportRequisition.setFileChecksum(vs.getFileChecksum());
        amortizationPostingReportRequisition.setTampered(vs.getTampered());
        amortizationPostingReportRequisition.setFilename(vs.getFilename());
        amortizationPostingReportRequisition.setReportParameters(vs.getReportParameters());
        amortizationPostingReportRequisition.setReportFile(vs.getReportFile());
        amortizationPostingReportRequisition.setReportFileContentType(vs.getReportFileContentType());
        amortizationPostingReportRequisition.setAmortizationPeriod(vs.getAmortizationPeriod());
        amortizationPostingReportRequisition.setRequestedBy(vs.getRequestedBy());
        amortizationPostingReportRequisition.setLastAccessedBy(vs.getLastAccessedBy());

        return amortizationPostingReportRequisition;
    }

    @Override
    public AttachedAmortizationPostingReportRequisition toValue2(AmortizationPostingReportRequisitionDTO vs) {
        return AttachedAmortizationPostingReportRequisition.builder()
            .id(vs.getId())
            .requestId(vs.getRequestId())
            .timeOfRequisition(vs.getTimeOfRequisition())
            .fileChecksum(vs.getFileChecksum())
            .tampered(vs.getTampered())
            .filename(vs.getFilename())
            .reportParameters(vs.getReportParameters())
            .reportFile(vs.getReportFile())
            .reportFileContentType(vs.getReportFileContentType())
            .amortizationPeriod(vs.getAmortizationPeriod())
            .requestedBy(vs.getRequestedBy())
            .lastAccessedBy(vs.getLastAccessedBy())
            .build();
    }
}
