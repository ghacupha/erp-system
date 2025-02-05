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

import io.github.erp.domain.WIPTransferListItemREPO;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.WIPTransferListItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class WIPTransferListItemMapping implements Mapping<WIPTransferListItemREPO, WIPTransferListItemDTO> {

    @Override
    public WIPTransferListItemREPO toValue1(WIPTransferListItemDTO vs) {

        return new WIPTransferListItemREPO() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public Long getWipSequence() {
                return vs.getWipSequence();
            }

            @Override
            public String getWipParticulars() {
                return vs.getWipParticulars();
            }

            @Override
            public String getTransferType() {
                return vs.getTransferType();
            }

            @Override
            public String getTransferSettlement() {
                return vs.getTransferSettlement();
            }

            @Override
            public LocalDate getTransferSettlementDate() {
                return vs.getTransferSettlementDate();
            }

            @Override
            public BigDecimal getTransferAmount() {
                return vs.getTransferAmount();
            }

            @Override
            public LocalDate getWipTransferDate() {
                return vs.getWipTransferDate();
            }

            @Override
            public String getOriginalSettlement() {
                return vs.getOriginalSettlement();
            }

            @Override
            public LocalDate getOriginalSettlementDate() {
                return vs.getOriginalSettlementDate();
            }

            @Override
            public String getAssetCategory() {
                return vs.getAssetCategory();
            }

            @Override
            public String getServiceOutlet() {
                return vs.getServiceOutlet();
            }

            @Override
            public String getWorkProject() {
                return vs.getWorkProject();
            }
        };
    }

    @Override
    public WIPTransferListItemDTO toValue2(WIPTransferListItemREPO vs) {

        WIPTransferListItemDTO dto = new WIPTransferListItemDTO();
        dto.setId(vs.getId());
        dto.setWipSequence(vs.getWipSequence());
        dto.setWipParticulars(vs.getWipParticulars());
        dto.setTransferType(vs.getTransferType());
        dto.setTransferSettlement(vs.getTransferSettlement());
        dto.setTransferSettlementDate(vs.getTransferSettlementDate());
        dto.setTransferAmount(vs.getTransferAmount());
        dto.setWipTransferDate(vs.getWipTransferDate());
        dto.setOriginalSettlement(vs.getOriginalSettlement());
        dto.setOriginalSettlementDate(vs.getOriginalSettlementDate());
        dto.setAssetCategory(vs.getAssetCategory());
        dto.setServiceOutlet(vs.getServiceOutlet());
        dto.setWorkProject(vs.getWorkProject());

        return dto;
    }
}
