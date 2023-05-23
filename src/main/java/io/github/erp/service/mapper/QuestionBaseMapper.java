package io.github.erp.service.mapper;

import io.github.erp.domain.QuestionBase;
import io.github.erp.service.dto.QuestionBaseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionBase} and its DTO {@link QuestionBaseDTO}.
 */
@Mapper(componentModel = "spring", uses = { UniversallyUniqueMappingMapper.class, PlaceholderMapper.class })
public interface QuestionBaseMapper extends EntityMapper<QuestionBaseDTO, QuestionBase> {
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "universalKeySet")
    @Mapping(target = "placeholderItems", source = "placeholderItems", qualifiedByName = "descriptionSet")
    QuestionBaseDTO toDto(QuestionBase s);

    @Mapping(target = "removeParameters", ignore = true)
    @Mapping(target = "removePlaceholderItem", ignore = true)
    QuestionBase toEntity(QuestionBaseDTO questionBaseDTO);
}
