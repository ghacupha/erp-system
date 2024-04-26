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
import io.github.erp.domain.DepreciationMethod;
import io.github.erp.service.dto.DepreciationMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepreciationMethod} and its DTO {@link DepreciationMethodDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface DepreciationMethodMapper extends EntityMapper<DepreciationMethodDTO, DepreciationMethod> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    DepreciationMethodDTO toDto(DepreciationMethod s);

    @Mapping(target = "removePlaceholder", ignore = true)
    DepreciationMethod toEntity(DepreciationMethodDTO depreciationMethodDTO);

    @Named("depreciationMethodName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "depreciationMethodName", source = "depreciationMethodName")
    DepreciationMethodDTO toDtoDepreciationMethodName(DepreciationMethod depreciationMethod);

    @Named("description")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    DepreciationMethodDTO toDtoDescription(DepreciationMethod depreciationMethod);
}
