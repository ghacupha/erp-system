package io.github.erp.internal.service.autonomousReport._maps;

import io.github.erp.domain.WorkInProgressOutstandingReportREPO;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.WorkInProgressOutstandingReportDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkInProgressOutstandingReportREPOMapper implements Mapping<WorkInProgressOutstandingReportREPO, WorkInProgressOutstandingReportDTO> {

    @Override
    public WorkInProgressOutstandingReportREPO toValue1(WorkInProgressOutstandingReportDTO vs) {
        return WorkInProgressOutstandingReportREPO.builder()
            .id(vs.getId())
            .sequenceNumber(vs.getSequenceNumber())
            .particulars(vs.getParticulars())
            .dealerName(vs.getDealerName())
            .iso4217Code(vs.getIso4217Code())
            .instalmentAmount(vs.getInstalmentAmount())
            .totalTransferAmount(vs.getTotalTransferAmount())
            .outstandingAmount(vs.getOutstandingAmount())
            .build();
    }

    @Override
    public WorkInProgressOutstandingReportDTO toValue2(WorkInProgressOutstandingReportREPO vs) {
        WorkInProgressOutstandingReportDTO dto = new WorkInProgressOutstandingReportDTO();

        dto.setId(vs.getId());
        dto.setSequenceNumber(vs.getSequenceNumber());
        dto.setParticulars(vs.getParticulars());
        dto.setDealerName(vs.getDealerName());
        dto.setIso4217Code(vs.getIso4217Code());
        dto.setInstalmentAmount(vs.getInstalmentAmount());
        dto.setTotalTransferAmount(vs.getTotalTransferAmount());
        dto.setOutstandingAmount(vs.getOutstandingAmount());

        return dto;
    }
}
