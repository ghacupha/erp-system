package io.github.erp.service.mapper;

import io.github.erp.domain.ReportRequisition;
import io.github.erp.service.dto.ReportRequisitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportRequisition} and its DTO {@link ReportRequisitionDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class, ReportTemplateMapper.class })
public interface ReportRequisitionMapper extends EntityMapper<ReportRequisitionDTO, ReportRequisition> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    @Mapping(target = "reportTemplate", source = "reportTemplate", qualifiedByName = "catalogueNumber")
    ReportRequisitionDTO toDto(ReportRequisition s);

    @Mapping(target = "removePlaceholders", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    ReportRequisition toEntity(ReportRequisitionDTO reportRequisitionDTO);
}
