package io.github.erp.internal.model.mapping;

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
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.RouAssetNBVReportItemInternal;
import io.github.erp.service.dto.RouAssetNBVReportItemDTO;
import org.springframework.stereotype.Component;

@Component
public class RouAssetNBVReportItemInternalMapper implements Mapping<RouAssetNBVReportItemInternal, RouAssetNBVReportItemDTO> {

    @Override
    public RouAssetNBVReportItemInternal toValue1(RouAssetNBVReportItemDTO vs) {
        throw new UnsupportedOperationException("This operation is not supported see docs for further information");
    }

    @Override
    public RouAssetNBVReportItemDTO toValue2(RouAssetNBVReportItemInternal vs) {
        RouAssetNBVReportItemDTO dto = new RouAssetNBVReportItemDTO();

        dto.setId(vs.getId());
        dto.setModelTitle(vs.getModelTitle());
        dto.setModelVersion(vs.getModelVersion());
        dto.setDescription(vs.getDescription());
        dto.setRouModelReference(vs.getRouModelReference());
        dto.setCommencementDate(vs.getCommencementDate());
        dto.setExpirationDate(vs.getExpirationDate());
        dto.setAssetCategoryName(vs.getAssetCategoryName());
        dto.setAssetAccountNumber(vs.getAssetAccountNumber());
        dto.setAssetAccountNumber(vs.getAssetAccountNumber());
        dto.setDepreciationAccountNumber(vs.getDepreciationAccountNumber());
        dto.setFiscalPeriodEndDate(vs.getFiscalPeriodEndDate());
        dto.setLeaseAmount(vs.getLeaseAmount());
        dto.setNetBookValue(vs.getNetBookValue());

        return dto;
    }
}
