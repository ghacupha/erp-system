package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.5
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
import io.github.erp.domain.ProcessStatus;
import io.github.erp.service.dto.ProcessStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProcessStatus} and its DTO {@link ProcessStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface ProcessStatusMapper extends EntityMapper<ProcessStatusDTO, ProcessStatus> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    ProcessStatusDTO toDto(ProcessStatus s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    ProcessStatus toEntity(ProcessStatusDTO processStatusDTO);

    @Named("statusCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "statusCode", source = "statusCode")
    ProcessStatusDTO toDtoStatusCode(ProcessStatus processStatus);
}
