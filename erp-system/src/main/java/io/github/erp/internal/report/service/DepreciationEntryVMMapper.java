package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
