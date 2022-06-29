package io.github.erp.service.mapper;

import io.github.erp.domain.ExcelReportExport;
import io.github.erp.service.dto.ExcelReportExportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExcelReportExport} and its DTO {@link ExcelReportExportDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface ExcelReportExportMapper extends EntityMapper<ExcelReportExportDTO, ExcelReportExport> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    ExcelReportExportDTO toDto(ExcelReportExport s);

    @Mapping(target = "removePlaceholder", ignore = true)
    ExcelReportExport toEntity(ExcelReportExportDTO excelReportExportDTO);
}
