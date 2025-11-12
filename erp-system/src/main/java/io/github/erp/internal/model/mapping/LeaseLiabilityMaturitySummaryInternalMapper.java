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
import io.github.erp.internal.model.LeaseLiabilityMaturitySummaryInternal;
import io.github.erp.service.dto.LeaseLiabilityMaturitySummaryDTO;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Maps the lease liability maturity summary projection to its DTO counterpart.
 */
@Component
public class LeaseLiabilityMaturitySummaryInternalMapper
    implements Mapping<LeaseLiabilityMaturitySummaryInternal, LeaseLiabilityMaturitySummaryDTO> {

    @Override
    public LeaseLiabilityMaturitySummaryInternal toValue1(LeaseLiabilityMaturitySummaryDTO vs) {
        throw new UnsupportedOperationException("Reverse mapping is not supported for the lease liability maturity summary");
    }

    @Override
    public LeaseLiabilityMaturitySummaryDTO toValue2(LeaseLiabilityMaturitySummaryInternal vs) {
        LeaseLiabilityMaturitySummaryDTO dto = new LeaseLiabilityMaturitySummaryDTO();
        dto.setMaturityLabel(vs.getMaturityLabel());
        dto.setLeasePrincipal(vs.getLeasePrincipal());
        dto.setInterestPayable(vs.getInterestPayable());
        BigDecimal total = vs.getTotal();
        if (total == null && vs.getLeasePrincipal() != null && vs.getInterestPayable() != null) {
            total = vs.getLeasePrincipal().add(vs.getInterestPayable());
        } else if (total == null && vs.getLeasePrincipal() != null) {
            total = vs.getLeasePrincipal();
        } else if (total == null && vs.getInterestPayable() != null) {
            total = vs.getInterestPayable();
        }
        dto.setTotal(total);
        return dto;
    }
}
