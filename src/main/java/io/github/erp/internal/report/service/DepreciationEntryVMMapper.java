package io.github.erp.internal.report.service;

import io.github.erp.domain.DepreciationEntry;
import io.github.erp.internal.framework.Mapping;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class DepreciationEntryVMMapper implements Mapping<DepreciationEntry, DepreciationEntryVM> {

    @Override
    public DepreciationEntry toValue1(DepreciationEntryVM vs) {
        // TODO ...
        return null;
    }

    @Override
    public DepreciationEntryVM toValue2(DepreciationEntry vs) {
        return DepreciationEntryVM
            .builder()
            .id(vs.getId())
            .assetRegistrationDetails(vs.getAssetRegistration().getAssetDetails())
            .postedAt(vs.getPostedAt().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            .assetNumber(String.valueOf(vs.getAssetNumber()))
            .serviceOutletCode(vs.getServiceOutlet().getOutletCode())
            .assetCategory(vs.getAssetCategory().getAssetCategoryName())
            .depreciationMethod(vs.getDepreciationMethod().getDepreciationMethodName())
            .depreciationPeriodCode(vs.getDepreciationPeriod().getPeriodCode())
            .fiscalMonthCode(vs.getFiscalMonth().getFiscalMonthCode())
            .assetRegistrationCost(vs.getAssetRegistration().getAssetCost())
            .depreciationAmount(vs.getDepreciationAmount())
            .build();
    }
}
