package io.github.erp.service.mapper;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
