package io.github.erp.service.mapper;

import io.github.erp.domain.ReportTemplate;
import io.github.erp.service.dto.ReportTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportTemplate} and its DTO {@link ReportTemplateDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface ReportTemplateMapper extends EntityMapper<ReportTemplateDTO, ReportTemplate> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    ReportTemplateDTO toDto(ReportTemplate s);

    @Mapping(target = "removePlaceholder", ignore = true)
    ReportTemplate toEntity(ReportTemplateDTO reportTemplateDTO);
}
