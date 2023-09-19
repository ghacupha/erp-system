package io.github.erp.service.mapper;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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

import io.github.erp.domain.ReportTemplate;
import io.github.erp.service.dto.ReportTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportTemplate} and its DTO {@link ReportTemplateDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface ReportTemplateMapper extends EntityMapper<ReportTemplateDTO, ReportTemplate> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    ReportTemplateDTO toDto(ReportTemplate s);

    @Mapping(target = "removePlaceholder", ignore = true)
    ReportTemplate toEntity(ReportTemplateDTO reportTemplateDTO);

    @Named("catalogueNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "catalogueNumber", source = "catalogueNumber")
    ReportTemplateDTO toDtoCatalogueNumber(ReportTemplate reportTemplate);
}
