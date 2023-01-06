package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0
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
import io.github.erp.domain.SystemModule;
import io.github.erp.service.dto.SystemModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemModule} and its DTO {@link SystemModuleDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface SystemModuleMapper extends EntityMapper<SystemModuleDTO, SystemModule> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "applicationMappings", source = "applicationMappings", qualifiedByName = "universalKeySet")
    SystemModuleDTO toDto(SystemModule s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeApplicationMapping", ignore = true)
    SystemModule toEntity(SystemModuleDTO systemModuleDTO);

    @Named("moduleName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "moduleName", source = "moduleName")
    SystemModuleDTO toDtoModuleName(SystemModule systemModule);
}
