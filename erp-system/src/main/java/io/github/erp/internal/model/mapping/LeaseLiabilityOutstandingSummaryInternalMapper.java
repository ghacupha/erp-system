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
import io.github.erp.internal.model.LeaseLiabilityOutstandingSummaryInternal;
import io.github.erp.service.dto.LeaseLiabilityOutstandingSummaryDTO;
import org.springframework.stereotype.Component;

/**
 * Maps the lease liability outstanding summary projection to its DTO counterpart.
 */
@Component
public class LeaseLiabilityOutstandingSummaryInternalMapper
    implements Mapping<LeaseLiabilityOutstandingSummaryInternal, LeaseLiabilityOutstandingSummaryDTO> {

    @Override
    public LeaseLiabilityOutstandingSummaryInternal toValue1(LeaseLiabilityOutstandingSummaryDTO vs) {
        throw new UnsupportedOperationException("Reverse mapping is not supported for the lease liability outstanding summary");
    }

    @Override
    public LeaseLiabilityOutstandingSummaryDTO toValue2(LeaseLiabilityOutstandingSummaryInternal vs) {
        LeaseLiabilityOutstandingSummaryDTO dto = new LeaseLiabilityOutstandingSummaryDTO();
        dto.setLeaseId(vs.getLeaseId());
        dto.setDealerName(vs.getDealerName());
        dto.setLiabilityAccount(vs.getLiabilityAccount());
        dto.setInterestPayableAccount(vs.getInterestPayableAccount());
        dto.setLeasePrincipal(vs.getLeasePrincipal());
        dto.setInterestPayable(vs.getInterestPayable());
        return dto;
    }
}
