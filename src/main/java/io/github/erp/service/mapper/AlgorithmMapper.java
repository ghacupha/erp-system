package io.github.erp.service.mapper;

import io.github.erp.domain.Algorithm;
import io.github.erp.service.dto.AlgorithmDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Algorithm} and its DTO {@link AlgorithmDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface AlgorithmMapper extends EntityMapper<AlgorithmDTO, Algorithm> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    AlgorithmDTO toDto(Algorithm s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    Algorithm toEntity(AlgorithmDTO algorithmDTO);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AlgorithmDTO toDtoName(Algorithm algorithm);
}
