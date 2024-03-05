package io.github.erp.internal.model.mapping;

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

import io.github.erp.domain.MonthlyPrepaymentOutstandingReportItemInternal;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.MonthlyPrepaymentOutstandingReportItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class MonthlyPrepaymentOutstandingReportInternalMapper
    implements Mapping<MonthlyPrepaymentOutstandingReportItemInternal,
    MonthlyPrepaymentOutstandingReportItemDTO> {


    @Override
    public MonthlyPrepaymentOutstandingReportItemInternal toValue1(MonthlyPrepaymentOutstandingReportItemDTO vs) {
        return new MonthlyPrepaymentOutstandingReportItemInternal() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public LocalDate getFiscalMonthEndDate() {

                return vs.getFiscalMonthEndDate();
            }

            @Override
            public BigDecimal getTotalPrepaymentAmount() {

                return vs.getTotalPrepaymentAmount();
            }

            @Override
            public BigDecimal getTotalAmortisedAmount() {

                return vs.getTotalAmortisedAmount();
            }

            @Override
            public BigDecimal getTotalOutstandingAmount() {

                return vs.getTotalOutstandingAmount();
            }

            @Override
            public Integer getNumberOfPrepaymentAccounts() {

                return vs.getNumberOfPrepaymentAccounts();
            }
        };
    }

    @Override
    public MonthlyPrepaymentOutstandingReportItemDTO toValue2(MonthlyPrepaymentOutstandingReportItemInternal vs) {

        MonthlyPrepaymentOutstandingReportItemDTO dto = new MonthlyPrepaymentOutstandingReportItemDTO();

        dto.setId(vs.getId());
        dto.setFiscalMonthEndDate(vs.getFiscalMonthEndDate());
        dto.setTotalPrepaymentAmount(vs.getTotalPrepaymentAmount());
        dto.setTotalAmortisedAmount(vs.getTotalAmortisedAmount());
        dto.setTotalOutstandingAmount(vs.getTotalOutstandingAmount());
        dto.setNumberOfPrepaymentAccounts(vs.getNumberOfPrepaymentAccounts());
        return dto;
    }
}
