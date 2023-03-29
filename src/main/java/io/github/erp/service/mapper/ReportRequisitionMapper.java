package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.2-SNAPSHOT
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
import io.github.erp.domain.ReportRequisition;
import io.github.erp.service.dto.ReportRequisitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportRequisition} and its DTO {@link ReportRequisitionDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class, ReportTemplateMapper.class, ReportContentTypeMapper.class }
)
public interface ReportRequisitionMapper extends EntityMapper<ReportRequisitionDTO, ReportRequisition> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    @Mapping(target = "reportTemplate", source = "reportTemplate", qualifiedByName = "catalogueNumber")
    @Mapping(target = "reportContentType", source = "reportContentType", qualifiedByName = "reportTypeName")
    ReportRequisitionDTO toDto(ReportRequisition s);

    @Mapping(target = "removePlaceholders", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    ReportRequisition toEntity(ReportRequisitionDTO reportRequisitionDTO);
}
