package io.github.erp.internal.report.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.DepreciationEntry;
import io.github.erp.internal.framework.Mapping;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class DepreciationEntryVMMapper implements Mapping<DepreciationEntry, DepreciationEntryVM> {

    @Override
    public DepreciationEntry toValue1(DepreciationEntryVM vs) {
        // TODO ...
        throw new UnsupportedOperationException();
        // return null;
    }

    @Override
    public DepreciationEntryVM toValue2(DepreciationEntry vs) {
        return DepreciationEntryVM
            .builder()
            .id(vs.getId())
            .assetRegistrationDetails(vs.getAssetRegistration().getAssetDetails())
            .postedAt(vs.getPostedAt().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            .assetNumber(String.valueOf(vs.getAssetNumber()))
            .serviceOutlet(vs.getServiceOutlet().getOutletCode())
            .assetCategory(vs.getAssetCategory().getAssetCategoryName())
            .depreciationMethod(vs.getDepreciationMethod().getDepreciationMethodName())
            .depreciationPeriod(vs.getDepreciationPeriod().getPeriodCode())
            .fiscalMonthCode(vs.getFiscalMonth().getFiscalMonthCode())
            .assetRegistrationCost(vs.getAssetRegistration().getAssetCost())
            .depreciationAmount(vs.getDepreciationAmount())
            .build();
    }
}
