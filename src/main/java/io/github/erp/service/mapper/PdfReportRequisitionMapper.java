package io.github.erp.service.mapper;

import io.github.erp.domain.PdfReportRequisition;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PdfReportRequisition} and its DTO {@link PdfReportRequisitionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ReportTemplateMapper.class, PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface PdfReportRequisitionMapper extends EntityMapper<PdfReportRequisitionDTO, PdfReportRequisition> {
    @Mapping(target = "reportTemplate", source = "reportTemplate", qualifiedByName = "catalogueNumber")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    PdfReportRequisitionDTO toDto(PdfReportRequisition s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    PdfReportRequisition toEntity(PdfReportRequisitionDTO pdfReportRequisitionDTO);
}
