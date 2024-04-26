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
