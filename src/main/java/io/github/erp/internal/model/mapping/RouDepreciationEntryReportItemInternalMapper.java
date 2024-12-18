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
import io.github.erp.internal.model.RouDepreciationEntryReportItemInternal;
import io.github.erp.service.dto.RouDepreciationEntryReportItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class RouDepreciationEntryReportItemInternalMapper implements Mapping<RouDepreciationEntryReportItemInternal, RouDepreciationEntryReportItemDTO> {

    @Override
    public RouDepreciationEntryReportItemInternal toValue1(RouDepreciationEntryReportItemDTO vs) {
        return new RouDepreciationEntryReportItemInternal() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getLeaseContractNumber() {
                return vs.getLeaseContractNumber();
            }

            @Override
            public String getFiscalPeriodCode() {
                return vs.getFiscalPeriodCode();
            }

            @Override
            public LocalDate getFiscalPeriodEndDate() {
                return vs.getFiscalPeriodEndDate();
            }

            @Override
            public String getAssetCategoryName() {
                return vs.getAssetCategoryName();
            }

            @Override
            public String getDebitAccountNumber() {
                return vs.getDebitAccountNumber();
            }

            @Override
            public String getCreditAccountNumber() {
                return vs.getCreditAccountNumber();
            }

            @Override
            public String getDescription() {
                return vs.getDescription();
            }

            @Override
            public String getShortTitle() {
                return vs.getShortTitle();
            }

            @Override
            public String getRouAssetIdentifier() {
                return vs.getRouAssetIdentifier();
            }

            @Override
            public Integer getSequenceNumber() {
                return vs.getSequenceNumber();
            }

            @Override
            public BigDecimal getDepreciationAmount() {
                return vs.getDepreciationAmount();
            }

            @Override
            public BigDecimal getOutstandingAmount() {
                return vs.getOutstandingAmount();
            }
        };
    }

    @Override
    public RouDepreciationEntryReportItemDTO toValue2(RouDepreciationEntryReportItemInternal vs) {
        RouDepreciationEntryReportItemDTO dto = new RouDepreciationEntryReportItemDTO();
        dto.setId(vs.getId());
        dto.setLeaseContractNumber(vs.getLeaseContractNumber());
        dto.setFiscalPeriodCode(vs.getFiscalPeriodCode());
        dto.setFiscalPeriodEndDate(vs.getFiscalPeriodEndDate());
        dto.setAssetCategoryName(vs.getAssetCategoryName());
        dto.setDebitAccountNumber(vs.getDebitAccountNumber());
        dto.setCreditAccountNumber(vs.getCreditAccountNumber());
        dto.setDescription(vs.getDescription());
        dto.setShortTitle(vs.getShortTitle());
        dto.setRouAssetIdentifier(vs.getRouAssetIdentifier());
        dto.setSequenceNumber(vs.getSequenceNumber());
        dto.setDepreciationAmount(vs.getDepreciationAmount());
        dto.setOutstandingAmount(vs.getOutstandingAmount());

        return dto;
    }
}
