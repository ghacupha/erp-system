package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.RouDepreciationEntryReportItemInternal;
import io.github.erp.service.dto.RouDepreciationPostingReportItemDTO;
import org.springframework.stereotype.Component;

@Component
public class RouDepreciationPostingReportItemDTOMapping implements Mapping<RouDepreciationEntryReportItemInternal, RouDepreciationPostingReportItemDTO> {


    @Override
    public RouDepreciationEntryReportItemInternal toValue1(RouDepreciationPostingReportItemDTO vs) {
        throw new IllegalArgumentException("This feature is disabled");
    }

    @Override
    public RouDepreciationPostingReportItemDTO toValue2(RouDepreciationEntryReportItemInternal vs) {
        RouDepreciationPostingReportItemDTO dto = new RouDepreciationPostingReportItemDTO();
        dto.setId(vs.getId());
        dto.setLeaseContractNumber(vs.getLeaseContractNumber());
        dto.setLeaseDescription(vs.getDescription());
        dto.setFiscalMonthCode(vs.getFiscalPeriodCode());
        dto.setAccountForCredit(vs.getCreditAccountNumber());
        dto.setAccountForDebit(vs.getDebitAccountNumber());
        dto.setDepreciationAmount(vs.getDepreciationAmount());

        return dto;
    }
}
