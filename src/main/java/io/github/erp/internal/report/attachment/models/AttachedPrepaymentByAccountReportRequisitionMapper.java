package io.github.erp.internal.report.attachment.models;

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
