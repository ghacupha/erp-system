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
import io.github.erp.domain.ReportStatus;
import io.github.erp.service.dto.ReportStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportStatus} and its DTO {@link ReportStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, ProcessStatusMapper.class })
public interface ReportStatusMapper extends EntityMapper<ReportStatusDTO, ReportStatus> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "processStatus", source = "processStatus", qualifiedByName = "statusCode")
    ReportStatusDTO toDto(ReportStatus s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportStatusDTO toDtoId(ReportStatus reportStatus);

    @Mapping(target = "removePlaceholder", ignore = true)
    ReportStatus toEntity(ReportStatusDTO reportStatusDTO);
}
