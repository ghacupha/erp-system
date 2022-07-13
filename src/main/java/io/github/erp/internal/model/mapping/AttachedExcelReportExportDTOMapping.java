package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.AttachedExcelReportExportDTO;
import io.github.erp.service.dto.ExcelReportExportDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachedExcelReportExportDTOMapping extends Mapping<ExcelReportExportDTO, AttachedExcelReportExportDTO> {
}
