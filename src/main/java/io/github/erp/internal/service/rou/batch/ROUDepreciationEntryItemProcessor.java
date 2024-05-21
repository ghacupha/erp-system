package io.github.erp.internal.service.rou.batch;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.internal.service.rou.ROUDepreciationEntryCompilationService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.dto.RouModelMetadataDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class ROUDepreciationEntryItemProcessor implements ItemProcessor<RouModelMetadataDTO, List<RouDepreciationEntryDTO>> {

    private final String batchJobIdentifier;
    private final ROUDepreciationEntryCompilationService rouDepreciationEntryCompilationService;

    public ROUDepreciationEntryItemProcessor(String batchJobIdentifier, ROUDepreciationEntryCompilationService rouDepreciationEntryCompilationService) {
        this.batchJobIdentifier = batchJobIdentifier;
        this.rouDepreciationEntryCompilationService = rouDepreciationEntryCompilationService;
    }

    @Override
    public List<RouDepreciationEntryDTO> process(@NotNull RouModelMetadataDTO model) {
        return rouDepreciationEntryCompilationService.compileDepreciationEntries(model, batchJobIdentifier);
    }
}
