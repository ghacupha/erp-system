package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.RouAccountBalanceReportItemInternal;
import io.github.erp.service.dto.RouAccountBalanceReportItemDTO;
import org.springframework.stereotype.Component;

@Component
public class RouAccountBalanceReportItemInternalMapper implements Mapping<RouAccountBalanceReportItemInternal, RouAccountBalanceReportItemDTO> {

    @Override
    public RouAccountBalanceReportItemInternal toValue1(RouAccountBalanceReportItemDTO vs) {
        throw new UnsupportedOperationException("This mapping is not supported, read the docs for further information");
    }

    @Override
    public RouAccountBalanceReportItemDTO toValue2(RouAccountBalanceReportItemInternal vs) {
        RouAccountBalanceReportItemDTO dto = new RouAccountBalanceReportItemDTO();

        dto.setId(vs.getId());
        dto.setAssetAccountName(vs.getAssetAccountName());
        dto.setAssetAccountNumber(vs.getAssetAccountNumber());
        dto.setDepreciationAccountNumber(vs.getDepreciationAccountNumber());
        dto.setTotalLeaseAmount(vs.getTotalLeaseAmount());
        dto.setAccruedDepreciationAmount(vs.getAccruedDepreciationAmount());
        dto.setCurrentPeriodDepreciationAmount(vs.getCurrentPeriodDepreciationAmount());
        dto.setNetBookValue(vs.getNetBookValue());
        dto.setFiscalPeriodEndDate(vs.getFiscalPeriodEndDate());
        return dto;
    }
}
