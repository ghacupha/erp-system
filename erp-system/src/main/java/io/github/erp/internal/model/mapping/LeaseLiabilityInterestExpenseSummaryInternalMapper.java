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
import io.github.erp.internal.model.LeaseLiabilityInterestExpenseSummaryInternal;
import io.github.erp.service.dto.LeaseLiabilityInterestExpenseSummaryDTO;
import org.springframework.stereotype.Component;

/**
 * Maps the native query projection for the lease liability interest expense summary report to the DTO exposed via the API.
 */
@Component
public class LeaseLiabilityInterestExpenseSummaryInternalMapper
    implements Mapping<LeaseLiabilityInterestExpenseSummaryInternal, LeaseLiabilityInterestExpenseSummaryDTO> {

    @Override
    public LeaseLiabilityInterestExpenseSummaryInternal toValue1(LeaseLiabilityInterestExpenseSummaryDTO vs) {
        throw new UnsupportedOperationException("Reverse mapping is not supported for the lease liability interest expense summary");
    }

    @Override
    public LeaseLiabilityInterestExpenseSummaryDTO toValue2(LeaseLiabilityInterestExpenseSummaryInternal vs) {
        LeaseLiabilityInterestExpenseSummaryDTO dto = new LeaseLiabilityInterestExpenseSummaryDTO();
        dto.setLeaseNumber(vs.getLeaseNumber());
        dto.setDealerName(vs.getDealerName());
        dto.setNarration(vs.getNarration());
        dto.setCreditAccount(vs.getCreditAccount());
        dto.setDebitAccount(vs.getDebitAccount());
        dto.setInterestExpense(vs.getInterestExpense());
        dto.setCumulativeAnnual(vs.getCumulativeAnnual());
        dto.setCumulativeLastMonth(vs.getCumulativeLastMonth());
        return dto;
    }
}
