package io.github.erp.service.mapper;

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

import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UniversallyUniqueMapping} and its DTO {@link UniversallyUniqueMappingDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface UniversallyUniqueMappingMapper extends EntityMapper<UniversallyUniqueMappingDTO, UniversallyUniqueMapping> {
    @Mapping(target = "parentMapping", source = "parentMapping", qualifiedByName = "universalKey")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    UniversallyUniqueMappingDTO toDto(UniversallyUniqueMapping s);

    @Mapping(target = "removePlaceholder", ignore = true)
    UniversallyUniqueMapping toEntity(UniversallyUniqueMappingDTO universallyUniqueMappingDTO);

    @Named("universalKeySet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "universalKey", source = "universalKey")
    Set<UniversallyUniqueMappingDTO> toDtoUniversalKeySet(Set<UniversallyUniqueMapping> universallyUniqueMapping);

    @Named("mappedValueSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "mappedValue", source = "mappedValue")
    Set<UniversallyUniqueMappingDTO> toDtoMappedValueSet(Set<UniversallyUniqueMapping> universallyUniqueMapping);

    @Named("universalKey")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "universalKey", source = "universalKey")
    UniversallyUniqueMappingDTO toDtoUniversalKey(UniversallyUniqueMapping universallyUniqueMapping);
}
