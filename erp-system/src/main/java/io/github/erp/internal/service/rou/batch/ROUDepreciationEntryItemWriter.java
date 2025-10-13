package io.github.erp.internal.service.rou.batch;

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
import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class ROUDepreciationEntryItemWriter implements ItemWriter<List<RouDepreciationEntryDTO>> {

    private final InternalRouDepreciationEntryService rouDepreciationEntryService;

    public ROUDepreciationEntryItemWriter(InternalRouDepreciationEntryService rouDepreciationEntryService) {
        this.rouDepreciationEntryService = rouDepreciationEntryService;
    }

    @Override
    public void write(List<? extends List<RouDepreciationEntryDTO>> items) throws Exception {
        for (List<RouDepreciationEntryDTO> entries : items) {
            rouDepreciationEntryService.save(entries);
        }
    }
}
