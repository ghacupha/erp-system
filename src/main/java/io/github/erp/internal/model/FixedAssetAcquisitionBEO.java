package io.github.erp.internal.model;

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
import io.github.erp.internal.framework.batch.BatchEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Batch entity representation for the fixed asset acquisition entity
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedAssetAcquisitionBEO implements BatchEntity {

    private Long id;

    private Long assetNumber;

    private String serviceOutletCode;

    private String assetTag;

    private String assetDescription;

    private LocalDate purchaseDate;

    private String assetCategory;

    private BigDecimal purchasePrice;

    private String fileUploadToken;

    @Override
    public void setUploadToken(String uploadToken) {
        this.fileUploadToken = uploadToken;
    }

    @Override
    public String getUploadToken() {
        return this.fileUploadToken;
    }
}
