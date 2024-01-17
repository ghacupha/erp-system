package io.github.erp.internal.report.service;

import io.github.erp.domain.DepreciationEntryInternal;
import io.github.erp.internal.framework.Mapping;
import org.springframework.stereotype.Component;

@Component
public class DepreciationEntryInternalMapper implements Mapping<DepreciationEntryInternal, DepreciationEntryVM> {

    @Override
    public DepreciationEntryInternal toValue1(DepreciationEntryVM vs) {
        // TODO
        return null;
    }

    @Override
    public DepreciationEntryVM toValue2(DepreciationEntryInternal vs) {
        return DepreciationEntryVM.builder()
            .id(vs.getGetId())
            .assetRegistrationDetails(vs.getAssetRegistrationDetails())
            .postedAt(vs.getPostedAt())
            .assetNumber(vs.getAssetNumber())
            .serviceOutletCode(vs.getServiceOutletCode())
            .assetCategory(vs.getAssetCategory())
            .depreciationMethod(vs.getDepreciationMethod())
            .depreciationPeriodCode(vs.getDepreciationPeriodCode())
            .fiscalMonthCode(vs.getFiscalMonthCode())
            .assetRegistrationCost(vs.getAssetRegistrationCost())
            .depreciationAmount(vs.getDepreciationAmount())
            .build();
    }
}
