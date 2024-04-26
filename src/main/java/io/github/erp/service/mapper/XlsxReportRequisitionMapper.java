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
