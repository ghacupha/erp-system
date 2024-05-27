package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.RouAssetNBVReportItemInternal;
import io.github.erp.service.dto.RouAssetNBVReportItemDTO;
import org.springframework.stereotype.Component;

@Component
public class RouAssetNBVReportItemInternalMapper implements Mapping<RouAssetNBVReportItemInternal, RouAssetNBVReportItemDTO> {

    @Override
    public RouAssetNBVReportItemInternal toValue1(RouAssetNBVReportItemDTO vs) {
        throw new UnsupportedOperationException("This operation is not supported see docs for further information");
    }

    @Override
    public RouAssetNBVReportItemDTO toValue2(RouAssetNBVReportItemInternal vs) {
        RouAssetNBVReportItemDTO dto = new RouAssetNBVReportItemDTO();

        dto.setId(vs.getId());
        dto.setModelTitle(vs.getModelTitle());
        dto.setModelVersion(vs.getModelVersion());
        dto.setDescription(vs.getDescription());
        dto.setRouModelReference(vs.getRouModelReference());
        dto.setCommencementDate(vs.getCommencementDate());
        dto.setExpirationDate(vs.getExpirationDate());
        dto.setAssetCategoryName(vs.getAssetCategoryName());
        dto.setAssetAccountNumber(vs.getAssetAccountNumber());
        dto.setAssetAccountNumber(vs.getAssetAccountNumber());
        dto.setDepreciationAccountNumber(vs.getDepreciationAccountNumber());
        dto.setFiscalPeriodEndDate(vs.getFiscalPeriodEndDate());
        dto.setLeaseAmount(vs.getLeaseAmount());
        dto.setNetBookValue(vs.getNetBookValue());

        return dto;
    }
}
