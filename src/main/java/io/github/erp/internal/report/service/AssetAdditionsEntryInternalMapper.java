package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.AssetAdditionsReportItemInternal;
import io.github.erp.internal.framework.Mapping;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class AssetAdditionsEntryInternalMapper implements Mapping<AssetAdditionsReportItemInternal, AssetsAdditionsReportItemVM> {

    @Override
    public AssetAdditionsReportItemInternal toValue1(AssetsAdditionsReportItemVM vs) {
        return new AssetAdditionsReportItemInternal() {
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
            public String getServiceOutletCode() {
                return vs.getServiceOutletCode();
            }

            @Override
            public String getTransactionId() {
                return vs.getTransactionId();
            }

            @Override
            public LocalDate getTransactionDate() {
                return vs.getTransactionDate();
            }

            @Override
            public LocalDate getCapitalizationDate() {
                return vs.getCapitalizationDate();
            }

            @Override
            public String getAssetCategory() {
                return vs.getAssetCategory();
            }

            @Override
            public String getAssetDetails() {
                return vs.getAssetDetails();
            }

            @Override
            public BigDecimal getAssetCost() {
                return vs.getAssetCost();
            }

            @Override
            public String getSupplier() {
                return vs.getSupplier();
            }

            @Override
            public BigDecimal getHistoricalCost() {
                return vs.getHistoricalCost();
            }

            @Override
            public LocalDate getRegistrationDate() {
                return vs.getRegistrationDate();
            }
        };
    }

    @Override
    public AssetsAdditionsReportItemVM toValue2(AssetAdditionsReportItemInternal vs) {
        return AssetsAdditionsReportItemVM.builder()
            .id(vs.getId())
            .assetNumber(vs.getAssetNumber())
            .assetTag(vs.getAssetTag())
            .serviceOutletCode(vs.getServiceOutletCode())
            .transactionId(vs.getTransactionId())
            .transactionDate(vs.getTransactionDate())
            .capitalizationDate(vs.getCapitalizationDate())
            .assetCategory(vs.getAssetCategory())
            .assetDetails(vs.getAssetDetails())
            .assetCost(vs.getAssetCost())
            .supplier(vs.getSupplier())
            .historicalCost(vs.getHistoricalCost())
            .registrationDate(vs.getRegistrationDate())
            .build();
    }
}
