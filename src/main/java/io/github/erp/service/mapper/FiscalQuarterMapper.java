package io.github.erp.service.mapper;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.domain.FiscalQuarter;
import io.github.erp.service.dto.FiscalQuarterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FiscalQuarter} and its DTO {@link FiscalQuarterDTO}.
 */
@Mapper(componentModel = "spring", uses = { FiscalYearMapper.class, PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface FiscalQuarterMapper extends EntityMapper<FiscalQuarterDTO, FiscalQuarter> {
    @Mapping(target = "fiscalYear", source = "fiscalYear", qualifiedByName = "fiscalYearCode")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    FiscalQuarterDTO toDto(FiscalQuarter s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FiscalQuarterDTO toDtoId(FiscalQuarter fiscalQuarter);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    FiscalQuarter toEntity(FiscalQuarterDTO fiscalQuarterDTO);

    @Named("fiscalQuarterCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fiscalQuarterCode", source = "fiscalQuarterCode")
    FiscalQuarterDTO toDtoFiscalQuarterCode(FiscalQuarter fiscalQuarter);
}
