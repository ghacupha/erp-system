package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import java.time.LocalDate;
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
                return vs.getNetBookValueAmount();
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
            public LocalDate getCapitalizationDate() {
                return vs.getCapitalizationDate();
            }

            @Override
            public String getServiceOutlet() {
                return vs.getServiceOutlet();
            }

            @Override
            public String getDepreciationPeriod() {
                return vs.getDepreciationPeriod();
            }

            @Override
            public String getFiscalMonth() {
                return vs.getFiscalMonth();
            }

            @Override
            public String getDepreciationMethod() {
                return vs.getDepreciationMethod();
            }

            @Override
            public String getAssetCategory() {
                return vs.getAssetCategory();
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
            .netBookValueAmount(vs.getNetBookValueAmount())
            .previousNetBookValueAmount(vs.getPreviousNetBookValueAmount())
            .historicalCost(vs.getHistoricalCost())
            .capitalizationDate(vs.getCapitalizationDate())
            .serviceOutlet(vs.getServiceOutlet())
            .depreciationPeriod(vs.getDepreciationPeriod())
            .fiscalMonth(vs.getFiscalMonth())
            .depreciationMethod(vs.getDepreciationMethod())
            .assetCategory(vs.getAssetCategory())
            .build();
    }
}
