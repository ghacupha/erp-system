package io.github.erp.internal.service.rou.batch;

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
import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

public class UpdateOutstandingAmountProcessor implements ItemProcessor<RouDepreciationEntryDTO, RouDepreciationEntryDTO> {

    private final InternalRouDepreciationEntryService depreciationEntryService;

    public UpdateOutstandingAmountProcessor(InternalRouDepreciationEntryService depreciationEntryService) {
        this.depreciationEntryService = depreciationEntryService;
    }

    @Override
    public RouDepreciationEntryDTO process(@NotNull RouDepreciationEntryDTO entry) {
        BigDecimal outstandingAmount = depreciationEntryService.calculateOutstandingAmount(entry);
        entry.setOutstandingAmount(outstandingAmount);
        return entry;
    }
}
