package io.github.erp.service.mapper;

import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UniversallyUniqueMapping} and its DTO {@link UniversallyUniqueMappingDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface UniversallyUniqueMappingMapper extends EntityMapper<UniversallyUniqueMappingDTO, UniversallyUniqueMapping> {
    @Mapping(target = "parentMapping", source = "parentMapping", qualifiedByName = "universalKey")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    UniversallyUniqueMappingDTO toDto(UniversallyUniqueMapping s);

    @Mapping(target = "removePlaceholder", ignore = true)
    UniversallyUniqueMapping toEntity(UniversallyUniqueMappingDTO universallyUniqueMappingDTO);

    @Named("universalKeySet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "universalKey", source = "universalKey")
    Set<UniversallyUniqueMappingDTO> toDtoUniversalKeySet(Set<UniversallyUniqueMapping> universallyUniqueMapping);

    @Named("mappedValueSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "mappedValue", source = "mappedValue")
    Set<UniversallyUniqueMappingDTO> toDtoMappedValueSet(Set<UniversallyUniqueMapping> universallyUniqueMapping);

    @Named("universalKey")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "universalKey", source = "universalKey")
    UniversallyUniqueMappingDTO toDtoUniversalKey(UniversallyUniqueMapping universallyUniqueMapping);
}
