package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.XlsxReportRequisition;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link XlsxReportRequisition} and its DTO {@link XlsxReportRequisitionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ReportTemplateMapper.class, PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface XlsxReportRequisitionMapper extends EntityMapper<XlsxReportRequisitionDTO, XlsxReportRequisition> {
    @Mapping(target = "reportTemplate", source = "reportTemplate", qualifiedByName = "catalogueNumber")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    XlsxReportRequisitionDTO toDto(XlsxReportRequisition s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    XlsxReportRequisition toEntity(XlsxReportRequisitionDTO xlsxReportRequisitionDTO);
}
