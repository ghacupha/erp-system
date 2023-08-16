package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.5
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.FiscalMonth;
import io.github.erp.service.dto.FiscalMonthDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FiscalMonth} and its DTO {@link FiscalMonthDTO}.
 */
@Mapper(componentModel = "spring", uses = { FiscalYearMapper.class, PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface FiscalMonthMapper extends EntityMapper<FiscalMonthDTO, FiscalMonth> {
    @Mapping(target = "fiscalYear", source = "fiscalYear", qualifiedByName = "fiscalYearCode")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    FiscalMonthDTO toDto(FiscalMonth s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    FiscalMonth toEntity(FiscalMonthDTO fiscalMonthDTO);
}
