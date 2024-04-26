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
import io.github.erp.domain.ExcelReportExport;
import io.github.erp.service.dto.ExcelReportExportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExcelReportExport} and its DTO {@link ExcelReportExportDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        UniversallyUniqueMappingMapper.class,
        ReportStatusMapper.class,
        SecurityClearanceMapper.class,
        ApplicationUserMapper.class,
        DealerMapper.class,
        SystemModuleMapper.class,
        ReportDesignMapper.class,
        AlgorithmMapper.class,
    }
)
public interface ExcelReportExportMapper extends EntityMapper<ExcelReportExportDTO, ExcelReportExport> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    @Mapping(target = "reportStatus", source = "reportStatus", qualifiedByName = "id")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "clearanceLevel")
    @Mapping(target = "reportCreator", source = "reportCreator", qualifiedByName = "applicationIdentity")
    @Mapping(target = "organization", source = "organization", qualifiedByName = "dealerName")
    @Mapping(target = "department", source = "department", qualifiedByName = "dealerName")
    @Mapping(target = "systemModule", source = "systemModule", qualifiedByName = "moduleName")
    @Mapping(target = "reportDesign", source = "reportDesign", qualifiedByName = "designation")
    @Mapping(target = "fileCheckSumAlgorithm", source = "fileCheckSumAlgorithm", qualifiedByName = "name")
    ExcelReportExportDTO toDto(ExcelReportExport s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    ExcelReportExport toEntity(ExcelReportExportDTO excelReportExportDTO);
}
