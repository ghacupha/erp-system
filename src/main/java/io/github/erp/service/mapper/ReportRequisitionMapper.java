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
