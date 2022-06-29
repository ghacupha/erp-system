package io.github.erp.service.mapper;

import io.github.erp.domain.ProcessStatus;
import io.github.erp.service.dto.ProcessStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProcessStatus} and its DTO {@link ProcessStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface ProcessStatusMapper extends EntityMapper<ProcessStatusDTO, ProcessStatus> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    ProcessStatusDTO toDto(ProcessStatus s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    ProcessStatus toEntity(ProcessStatusDTO processStatusDTO);
}
