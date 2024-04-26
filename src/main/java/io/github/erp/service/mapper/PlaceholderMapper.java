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
import io.github.erp.domain.Placeholder;
import io.github.erp.service.dto.PlaceholderDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Placeholder} and its DTO {@link PlaceholderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlaceholderMapper extends EntityMapper<PlaceholderDTO, Placeholder> {
    @Mapping(target = "containingPlaceholder", source = "containingPlaceholder", qualifiedByName = "description")
    PlaceholderDTO toDto(Placeholder s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<PlaceholderDTO> toDtoIdSet(Set<Placeholder> placeholder);

    @Named("description")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    PlaceholderDTO toDtoDescription(Placeholder placeholder);

    @Named("descriptionSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    Set<PlaceholderDTO> toDtoDescriptionSet(Set<Placeholder> placeholder);
}
