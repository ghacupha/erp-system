package io.github.erp.service.mapper;

import io.github.erp.domain.ReportStatus;
import io.github.erp.service.dto.ReportStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportStatus} and its DTO {@link ReportStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface ReportStatusMapper extends EntityMapper<ReportStatusDTO, ReportStatus> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    ReportStatusDTO toDto(ReportStatus s);

    @Mapping(target = "removePlaceholder", ignore = true)
    ReportStatus toEntity(ReportStatusDTO reportStatusDTO);
}
