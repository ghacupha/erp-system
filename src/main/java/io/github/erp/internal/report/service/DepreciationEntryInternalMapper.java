package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.DepreciationEntryInternal;
import io.github.erp.internal.framework.Mapping;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepreciationEntryInternalMapper implements Mapping<DepreciationEntryInternal, DepreciationEntryVM> {

    @Override
    public DepreciationEntryInternal toValue1(DepreciationEntryVM vs) {
        // TODO
        return new DepreciationEntryInternal() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getAssetRegistrationDetails() {
                return vs.getAssetRegistrationDetails();
            }

            @Override
            public String getPostedAt() {
                return vs.getPostedAt();
            }

            @Override
            public String getAssetNumber() {
                return vs.getAssetNumber();
            }

            @Override
            public String getServiceOutletCode() {
                return vs.getServiceOutletCode();
            }

            @Override
            public String getAssetCategory() {
                return vs.getAssetCategory();
            }

            @Override
            public String getDepreciationMethod() {
                return vs.getDepreciationMethod();
            }

            @Override
            public String getDepreciationPeriodCode() {
                return vs.getDepreciationPeriodCode();
            }

            @Override
            public String getFiscalMonthCode() {
                return vs.getFiscalMonthCode();
            }

            @Override
            public BigDecimal getAssetRegistrationCost() {
                return vs.getAssetRegistrationCost();
            }

            @Override
            public BigDecimal getDepreciationAmount() {
                return vs.getDepreciationAmount();
            }
        };
    }

    @Override
    public DepreciationEntryVM toValue2(DepreciationEntryInternal vs) {
        return DepreciationEntryVM.builder()
            .id(vs.getId())
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
