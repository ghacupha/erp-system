package io.github.erp.service.mapper;

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
    }
)
public interface ExcelReportExportMapper extends EntityMapper<ExcelReportExportDTO, ExcelReportExport> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    @Mapping(target = "reportStatus", source = "reportStatus", qualifiedByName = "id")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "clearanceLevel")
    @Mapping(target = "reportCreator", source = "reportCreator", qualifiedByName = "id")
    @Mapping(target = "organization", source = "organization", qualifiedByName = "dealerName")
    @Mapping(target = "department", source = "department", qualifiedByName = "dealerName")
    @Mapping(target = "systemModule", source = "systemModule", qualifiedByName = "moduleName")
    @Mapping(target = "reportDesign", source = "reportDesign", qualifiedByName = "designation")
    ExcelReportExportDTO toDto(ExcelReportExport s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    ExcelReportExport toEntity(ExcelReportExportDTO excelReportExportDTO);
}
