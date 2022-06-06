package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.AttachedPdfReportRequisitionDTO;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachedPdfReportRequisitionDTOMapping extends Mapping<PdfReportRequisitionDTO, AttachedPdfReportRequisitionDTO> {
}
