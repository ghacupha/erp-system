package io.github.erp.service.mapper;

import io.github.erp.domain.SystemContentType;
import io.github.erp.service.dto.SystemContentTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemContentType} and its DTO {@link SystemContentTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface SystemContentTypeMapper extends EntityMapper<SystemContentTypeDTO, SystemContentType> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "sysMaps", source = "sysMaps", qualifiedByName = "mappedValueSet")
    SystemContentTypeDTO toDto(SystemContentType s);

    @Mapping(target = "removePlaceholders", ignore = true)
    @Mapping(target = "removeSysMaps", ignore = true)
    SystemContentType toEntity(SystemContentTypeDTO systemContentTypeDTO);

    @Named("contentTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "contentTypeName", source = "contentTypeName")
    SystemContentTypeDTO toDtoContentTypeName(SystemContentType systemContentType);
}
