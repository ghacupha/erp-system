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
import io.github.erp.internal.model.LeaseInterestPaidTransferSummaryInternal;
import io.github.erp.service.dto.LeaseInterestPaidTransferSummaryDTO;
import org.springframework.stereotype.Component;

/**
 * Maps the lease interest paid transfer summary projection to its DTO counterpart.
 */
@Component
public class LeaseInterestPaidTransferSummaryInternalMapper
    implements Mapping<LeaseInterestPaidTransferSummaryInternal, LeaseInterestPaidTransferSummaryDTO> {

    @Override
    public LeaseInterestPaidTransferSummaryInternal toValue1(LeaseInterestPaidTransferSummaryDTO vs) {
        throw new UnsupportedOperationException("Reverse mapping is not supported for the lease interest paid transfer summary");
    }

    @Override
    public LeaseInterestPaidTransferSummaryDTO toValue2(LeaseInterestPaidTransferSummaryInternal vs) {
        LeaseInterestPaidTransferSummaryDTO dto = new LeaseInterestPaidTransferSummaryDTO();
        dto.setLeaseId(vs.getLeaseId());
        dto.setDealerName(vs.getDealerName());
        dto.setNarration(vs.getNarration());
        dto.setCreditAccount(vs.getCreditAccount());
        dto.setDebitAccount(vs.getDebitAccount());
        dto.setInterestAmount(vs.getInterestAmount());
        return dto;
    }
}
