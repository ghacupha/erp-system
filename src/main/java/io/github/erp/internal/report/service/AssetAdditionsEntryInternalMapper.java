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
