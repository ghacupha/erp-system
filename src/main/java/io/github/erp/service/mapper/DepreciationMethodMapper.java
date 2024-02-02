package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
