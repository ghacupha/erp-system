package io.github.erp.service.mapper;

import io.github.erp.domain.ReportContentType;
import io.github.erp.service.dto.ReportContentTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportContentType} and its DTO {@link ReportContentTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = { SystemContentTypeMapper.class, PlaceholderMapper.class })
public interface ReportContentTypeMapper extends EntityMapper<ReportContentTypeDTO, ReportContentType> {
    @Mapping(target = "systemContentType", source = "systemContentType", qualifiedByName = "contentTypeName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    ReportContentTypeDTO toDto(ReportContentType s);

    @Mapping(target = "removePlaceholder", ignore = true)
    ReportContentType toEntity(ReportContentTypeDTO reportContentTypeDTO);
}
