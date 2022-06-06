package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.AttachedXlsxReportRequisitionDTO;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachedXlsxReportRequisitionDTOMapping extends Mapping<XlsxReportRequisitionDTO, AttachedXlsxReportRequisitionDTO> {
}
