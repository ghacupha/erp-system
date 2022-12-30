package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 7 (Caleb Series) Server ver 0.3.0-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.CountyCode;
import io.github.erp.service.dto.CountyCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CountyCode} and its DTO {@link CountyCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface CountyCodeMapper extends EntityMapper<CountyCodeDTO, CountyCode> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    CountyCodeDTO toDto(CountyCode s);

    @Mapping(target = "removePlaceholder", ignore = true)
    CountyCode toEntity(CountyCodeDTO countyCodeDTO);

    @Named("countyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "countyName", source = "countyName")
    CountyCodeDTO toDtoCountyName(CountyCode countyCode);

    @Named("subCountyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "subCountyName", source = "subCountyName")
    CountyCodeDTO toDtoSubCountyName(CountyCode countyCode);
}
