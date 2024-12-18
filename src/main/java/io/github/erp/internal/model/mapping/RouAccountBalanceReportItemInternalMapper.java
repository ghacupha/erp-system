package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.internal.model.RouAccountBalanceReportItemInternal;
import io.github.erp.service.dto.RouAccountBalanceReportItemDTO;
import org.springframework.stereotype.Component;

@Component
public class RouAccountBalanceReportItemInternalMapper implements Mapping<RouAccountBalanceReportItemInternal, RouAccountBalanceReportItemDTO> {

    @Override
    public RouAccountBalanceReportItemInternal toValue1(RouAccountBalanceReportItemDTO vs) {
        throw new UnsupportedOperationException("This mapping is not supported, read the docs for further information");
    }

    @Override
    public RouAccountBalanceReportItemDTO toValue2(RouAccountBalanceReportItemInternal vs) {
        RouAccountBalanceReportItemDTO dto = new RouAccountBalanceReportItemDTO();

        dto.setId(vs.getId());
        dto.setAssetAccountName(vs.getAssetAccountName());
        dto.setAssetAccountNumber(vs.getAssetAccountNumber());
        dto.setDepreciationAccountNumber(vs.getDepreciationAccountNumber());
        dto.setTotalLeaseAmount(vs.getTotalLeaseAmount());
        dto.setAccruedDepreciationAmount(vs.getAccruedDepreciationAmount());
        dto.setCurrentPeriodDepreciationAmount(vs.getCurrentPeriodDepreciationAmount());
        dto.setNetBookValue(vs.getNetBookValue());
        dto.setFiscalPeriodEndDate(vs.getFiscalPeriodEndDate());
        return dto;
    }
}
