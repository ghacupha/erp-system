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
import io.github.erp.domain.DepreciationEntryInternal;
import io.github.erp.internal.framework.Mapping;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

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
            public String getServiceOutlet() {
                return vs.getServiceOutlet();
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
            public String getDepreciationPeriod() {
                return vs.getDepreciationPeriod();
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

            @Override
            public Long getElapsedMonths() {
                return vs.getElapsedMonths();
            }

            @Override
            public Long getPriorMonths() {
                return vs.getPriorMonths();
            }

            @Override
            public BigDecimal getUsefulLifeYears() {
                return vs.getUsefulLifeYears();
            }

            @Override
            public BigDecimal getPreviousNBV() {
                return vs.getPreviousNBV();
            }

            @Override
            public BigDecimal getNetBookValue() {
                return vs.getNetBookValue();
            }

            @Override
            public LocalDate getDepreciationPeriodStartDate() {
                return vs.getDepreciationPeriodStartDate();
            }

            @Override
            public LocalDate getDepreciationPeriodEndDate() {
                return vs.getDepreciationPeriodEndDate();
            }

            @Override
            public LocalDate getCapitalizationDate() {
                return vs.getCapitalizationDate();
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
            .serviceOutlet(vs.getServiceOutlet())
            .assetCategory(vs.getAssetCategory())
            .depreciationMethod(vs.getDepreciationMethod())
            .depreciationPeriod(vs.getDepreciationPeriod())
            .fiscalMonthCode(vs.getFiscalMonthCode())
            .assetRegistrationCost(vs.getAssetRegistrationCost())
            .depreciationAmount(vs.getDepreciationAmount())
            .elapsedMonths(vs.getElapsedMonths())
            .priorMonths(vs.getPriorMonths())
            .usefulLifeYears(vs.getUsefulLifeYears())
            .previousNBV(vs.getPreviousNBV())
            .netBookValue(vs.getNetBookValue())
            .depreciationPeriodStartDate(vs.getDepreciationPeriodStartDate())
            .depreciationPeriodEndDate(vs.getDepreciationPeriodEndDate())
            .capitalizationDate(vs.getCapitalizationDate())
            .build();
    }
}
