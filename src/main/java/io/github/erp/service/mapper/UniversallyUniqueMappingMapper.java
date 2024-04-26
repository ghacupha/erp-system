package io.github.erp.service.mapper;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
