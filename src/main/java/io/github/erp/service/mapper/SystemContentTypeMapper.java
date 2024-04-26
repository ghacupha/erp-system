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
import io.github.erp.domain.SystemContentType;
import io.github.erp.service.dto.SystemContentTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemContentType} and its DTO {@link SystemContentTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface SystemContentTypeMapper extends EntityMapper<SystemContentTypeDTO, SystemContentType> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "sysMaps", source = "sysMaps", qualifiedByName = "mappedValueSet")
    SystemContentTypeDTO toDto(SystemContentType s);

    @Mapping(target = "removePlaceholders", ignore = true)
    @Mapping(target = "removeSysMaps", ignore = true)
    SystemContentType toEntity(SystemContentTypeDTO systemContentTypeDTO);

    @Named("contentTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "contentTypeName", source = "contentTypeName")
    SystemContentTypeDTO toDtoContentTypeName(SystemContentType systemContentType);
}
