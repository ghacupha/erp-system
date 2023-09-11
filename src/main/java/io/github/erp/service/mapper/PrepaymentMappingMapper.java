package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
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

import io.github.erp.domain.PrepaymentMapping;
import io.github.erp.service.dto.PrepaymentMappingDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrepaymentMapping} and its DTO {@link PrepaymentMappingDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface PrepaymentMappingMapper extends EntityMapper<PrepaymentMappingDTO, PrepaymentMapping> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    PrepaymentMappingDTO toDto(PrepaymentMapping s);

    @Mapping(target = "removePlaceholder", ignore = true)
    PrepaymentMapping toEntity(PrepaymentMappingDTO prepaymentMappingDTO);

    @Named("parameterKeySet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "parameterKey", source = "parameterKey")
    Set<PrepaymentMappingDTO> toDtoParameterKeySet(Set<PrepaymentMapping> prepaymentMapping);

    @Named("parameterSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "parameter", source = "parameter")
    Set<PrepaymentMappingDTO> toDtoParameterSet(Set<PrepaymentMapping> prepaymentMapping);
}
