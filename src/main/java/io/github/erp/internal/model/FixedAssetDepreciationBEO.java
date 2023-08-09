package io.github.erp.internal.model;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.DepreciationRegime;
import io.github.erp.internal.framework.batch.BatchEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Batch entity object for the fixed-asset depreciation entity
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedAssetDepreciationBEO  implements BatchEntity {

    private Long id;

    private Long assetNumber;

    private String serviceOutletCode;

    private String assetTag;

    private String assetDescription;

    private LocalDate depreciationDate;

    private String assetCategory;

    private BigDecimal depreciationAmount;

    private DepreciationRegime depreciationRegime;

    private String fileUploadToken;

    private String compilationToken;

    @Override
    public void setUploadToken(String uploadToken) {
        this.fileUploadToken = uploadToken;
    }

    @Override
    public String getUploadToken() {
        return this.fileUploadToken;
    }
}
