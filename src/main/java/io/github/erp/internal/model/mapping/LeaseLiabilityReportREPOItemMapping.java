package io.github.erp.internal.model.mapping;

import io.github.erp.internal.model.LeaseLiabilityReportItemREPO;
import io.github.erp.service.dto.LeaseLiabilityReportItemDTO;
import org.springframework.stereotype.Component;

@Component
public class LeaseLiabilityReportREPOItemMapping implements LeaseLiabilityReportREPOItemMapper {

    @Override
    public LeaseLiabilityReportItemDTO toValue1(LeaseLiabilityReportItemREPO vs) {
        var dto = new LeaseLiabilityReportItemDTO();

        dto.setId(vs.getId());
        dto.setLeaseTitle(vs.getLeaseTitle());
        dto.setBookingId(vs.getBookingId());
        dto.setLiabilityAccountNumber(vs.getLiabilityAccountNumber());
        dto.setLiabilityAmount(vs.getLiabilityAmount());
        dto.setInterestPayableAccountNumber(vs.getInterestPayableAccountNumber());
        dto.setInterestPayableAmount(vs.getInterestPayableAmount());

        return dto;
    }

    @Override
    public LeaseLiabilityReportItemREPO toValue2(LeaseLiabilityReportItemDTO vs) {
        return null;
    }
}
