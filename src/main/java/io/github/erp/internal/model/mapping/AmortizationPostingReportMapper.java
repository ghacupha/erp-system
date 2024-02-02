package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import io.github.erp.domain.AmortizationPostingReportInternal;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AmortizationPostingReportMapper implements Mapping<AmortizationPostingReportInternal, AmortizationPostingReportDTO> {

    @Override
    public AmortizationPostingReportInternal toValue1(AmortizationPostingReportDTO vs) {
        return new AmortizationPostingReportInternal() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getCatalogueNumber() {
                return vs.getCatalogueNumber();
            }

            @Override
            public String getDebitAccount() {
                return vs.getDebitAccount();
            }

            @Override
            public String getCreditAccount() {
                return vs.getCreditAccount();
            }

            @Override
            public String getDescription() {
                return vs.getDescription();
            }

            @Override
            public BigDecimal getAmortizationAmount() {
                return vs.getAmortizationAmount();
            }
        };
    }

    @Override
    public AmortizationPostingReportDTO toValue2(AmortizationPostingReportInternal vs) {
        AmortizationPostingReportDTO dto = new AmortizationPostingReportDTO();
        dto.setId(vs.getId());
        dto.setCatalogueNumber(vs.getCatalogueNumber());
        dto.setDebitAccount(vs.getDebitAccount());
        dto.setCreditAccount(vs.getCreditAccount());
        dto.setDescription(vs.getDescription());
        dto.setAmortizationAmount(vs.getAmortizationAmount());

        return dto;
    }
}
