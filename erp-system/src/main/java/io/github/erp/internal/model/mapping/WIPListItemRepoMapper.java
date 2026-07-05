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
import io.github.erp.domain.WIPListItemREPO;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.WIPListItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component("wipListItemRepoMapper")
public class WIPListItemRepoMapper implements Mapping<WIPListItemREPO, WIPListItemDTO> {

    @Override
    public WIPListItemREPO toValue1(WIPListItemDTO vs) {

        return new WIPListItemREPO() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getSequenceNumber() {
                return vs.getSequenceNumber();
            }

            @Override
            public String getParticulars() {
                return vs.getParticulars();
            }

            @Override
            public LocalDate getInstalmentDate() {
                return vs.getInstalmentDate();
            }

            @Override
            public BigDecimal getInstalmentAmount() {
                return vs.getInstalmentAmount();
            }

            @Override
            public String getSettlementCurrency() {
                return vs.getSettlementCurrency();
            }

            @Override
            public String getOutletCode() {
                return vs.getOutletCode();
            }

            @Override
            public String getSettlementTransaction() {
                return vs.getSettlementTransaction();
            }

            @Override
            public LocalDate getSettlementTransactionDate() {
                return vs.getSettlementTransactionDate();
            }

            @Override
            public String getDealerName() {
                return vs.getDealerName();
            }

            @Override
            public String getWorkProject() {
                return vs.getWorkProject();
            }
        };
    }

    @Override
    public WIPListItemDTO toValue2(WIPListItemREPO vs) {
        WIPListItemDTO dto = new WIPListItemDTO();
        dto.setId(vs.getId());
        dto.setSequenceNumber(vs.getSequenceNumber());
        dto.setParticulars(vs.getParticulars());
        dto.setInstalmentDate(vs.getInstalmentDate());
        dto.setInstalmentAmount(vs.getInstalmentAmount());
        dto.setSettlementCurrency(vs.getSettlementCurrency());
        dto.setSettlementTransaction(vs.getSettlementTransaction());
        dto.setSettlementTransactionDate(vs.getSettlementTransactionDate());
        dto.setDealerName(vs.getDealerName());
        dto.setWorkProject(vs.getWorkProject());

        return dto;
    }
}
