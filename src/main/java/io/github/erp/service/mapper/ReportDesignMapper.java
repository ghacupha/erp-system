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
import io.github.erp.domain.ReportDesign;
import io.github.erp.service.dto.ReportDesignDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportDesign} and its DTO {@link ReportDesignDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        UniversallyUniqueMappingMapper.class,
        SecurityClearanceMapper.class,
        ApplicationUserMapper.class,
        DealerMapper.class,
        PlaceholderMapper.class,
        SystemModuleMapper.class,
        AlgorithmMapper.class,
    }
)
public interface ReportDesignMapper extends EntityMapper<ReportDesignDTO, ReportDesign> {
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "clearanceLevel")
    @Mapping(target = "reportDesigner", source = "reportDesigner", qualifiedByName = "applicationIdentity")
    @Mapping(target = "organization", source = "organization", qualifiedByName = "dealerName")
    @Mapping(target = "department", source = "department", qualifiedByName = "dealerName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "systemModule", source = "systemModule", qualifiedByName = "moduleName")
    @Mapping(target = "fileCheckSumAlgorithm", source = "fileCheckSumAlgorithm", qualifiedByName = "name")
    ReportDesignDTO toDto(ReportDesign s);

    @Mapping(target = "removeParameters", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    ReportDesign toEntity(ReportDesignDTO reportDesignDTO);

    @Named("designation")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designation", source = "designation")
    ReportDesignDTO toDtoDesignation(ReportDesign reportDesign);
}
