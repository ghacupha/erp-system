package io.github.erp.service.mapper;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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

import io.github.erp.domain.ReportContentType;
import io.github.erp.service.dto.ReportContentTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportContentType} and its DTO {@link ReportContentTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = { SystemContentTypeMapper.class, PlaceholderMapper.class })
public interface ReportContentTypeMapper extends EntityMapper<ReportContentTypeDTO, ReportContentType> {
    @Mapping(target = "systemContentType", source = "systemContentType", qualifiedByName = "contentTypeName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    ReportContentTypeDTO toDto(ReportContentType s);

    @Mapping(target = "removePlaceholder", ignore = true)
    ReportContentType toEntity(ReportContentTypeDTO reportContentTypeDTO);

    @Named("reportTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "reportTypeName", source = "reportTypeName")
    ReportContentTypeDTO toDtoReportTypeName(ReportContentType reportContentType);
}
