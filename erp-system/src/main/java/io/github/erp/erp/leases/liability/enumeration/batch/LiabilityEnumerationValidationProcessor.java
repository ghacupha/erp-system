package io.github.erp.erp.leases.liability.enumeration.batch;

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
import io.github.erp.domain.enumeration.LiabilityTimeGranularity;
import java.math.BigDecimal;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("liabilityEnumerationValidationProcessor")
public class LiabilityEnumerationValidationProcessor implements ItemProcessor<LiabilityEnumerationBatchItem, LiabilityEnumerationBatchItem> {

    @Override
    public LiabilityEnumerationBatchItem process(LiabilityEnumerationBatchItem item) {
        BigDecimal annualRate = new BigDecimal(item.getRequest().getInterestRate());
        if (annualRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate must be zero or positive");
        }

        LiabilityTimeGranularity granularity = LiabilityTimeGranularity.fromCode(item.getRequest().getTimeGranularity());
        item.setAnnualRate(annualRate);
        item.setGranularity(granularity);
        return item;
    }
}
