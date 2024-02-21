package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.NetBookValueEntryInternal;
import io.github.erp.internal.framework.Mapping;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class NetBookValueEntryVMMapper implements Mapping<NetBookValueEntryInternal, NetBookValueEntryVM> {

    @Override
    public NetBookValueEntryInternal toValue1(NetBookValueEntryVM vs) {
        return new NetBookValueEntryInternal() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getAssetNumber() {
                return vs.getAssetNumber();
            }

            @Override
            public String getAssetTag() {
                return vs.getAssetTag();
            }

            @Override
            public String getAssetDescription() {
                return vs.getAssetDescription();
            }

            @Override
            public UUID getNbvIdentifier() {
                return vs.getNbvIdentifier();
            }

            @Override
            public UUID getCompilationJobIdentifier() {
                return vs.getCompilationJobIdentifier();
            }

            @Override
            public UUID getCompilationBatchIdentifier() {
                return vs.getCompilationBatchIdentifier();
            }

            @Override
            public Integer getElapsedMonths() {
                return vs.getElapsedMonths();
            }

            @Override
            public Integer getPriorMonths() {
                return vs.getPriorMonths();
            }

            @Override
            public Integer getUsefulLifeYears() {
                return vs.getUsefulLifeYears();
            }

            @Override
            public BigDecimal getNetBookValueAmount() {
                return vs.getNNetBookValueAmount();
            }

            @Override
            public BigDecimal getPreviousNetBookValueAmount() {
                return vs.getPreviousNetBookValueAmount();
            }

            @Override
            public BigDecimal getHistoricalCost() {
                return vs.getHistoricalCost();
            }

            @Override
            public String getServiceOutletCode() {
                return vs.getServiceOutletCode();
            }

            @Override
            public String getDepreciationPeriodCode() {
                return vs.getDepreciationPeriodCode();
            }

            @Override
            public FiscalMonth getFiscalMonthCode() {
                return vs.getFiscalMonthCode();
            }

            @Override
            public String getDepreciationMethodName() {
                return vs.getDepreciationMethodName();
            }

            @Override
            public String getAssetCategoryName() {
                return vs.getAssetCategoryName();
            }
        };
    }

    @Override
    public NetBookValueEntryVM toValue2(NetBookValueEntryInternal vs) {
        return NetBookValueEntryVM.builder()
            .id(vs.getId())
            .assetNumber(vs.getAssetNumber())
            .assetTag(vs.getAssetTag())
            .assetDescription(vs.getAssetDescription())
            .nbvIdentifier(vs.getNbvIdentifier())
            .compilationJobIdentifier(vs.getCompilationJobIdentifier())
            .compilationBatchIdentifier(vs.getCompilationBatchIdentifier())
            .elapsedMonths(vs.getElapsedMonths())
            .priorMonths(vs.getPriorMonths())
            .usefulLifeYears(vs.getUsefulLifeYears())
            .previousNetBookValueAmount(vs.getPreviousNetBookValueAmount())
            .historicalCost(vs.getHistoricalCost())
            .serviceOutletCode(vs.getServiceOutletCode())
            .depreciationPeriodCode(vs.getDepreciationPeriodCode())
            .fiscalMonthCode(vs.getFiscalMonthCode())
            .depreciationMethodName(vs.getDepreciationMethodName())
            .assetCategoryName(vs.getAssetCategoryName())
            .build();
    }
}
