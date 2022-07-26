package io.github.erp.service.mapper;

import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UniversallyUniqueMapping} and its DTO {@link UniversallyUniqueMappingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UniversallyUniqueMappingMapper extends EntityMapper<UniversallyUniqueMappingDTO, UniversallyUniqueMapping> {
    @Named("mappedValueSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "mappedValue", source = "mappedValue")
    Set<UniversallyUniqueMappingDTO> toDtoMappedValueSet(Set<UniversallyUniqueMapping> universallyUniqueMapping);
}
