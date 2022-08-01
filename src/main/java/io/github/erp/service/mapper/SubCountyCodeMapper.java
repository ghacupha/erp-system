package io.github.erp.service.mapper;

import io.github.erp.domain.SubCountyCode;
import io.github.erp.service.dto.SubCountyCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubCountyCode} and its DTO {@link SubCountyCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface SubCountyCodeMapper extends EntityMapper<SubCountyCodeDTO, SubCountyCode> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    SubCountyCodeDTO toDto(SubCountyCode s);

    @Mapping(target = "removePlaceholder", ignore = true)
    SubCountyCode toEntity(SubCountyCodeDTO subCountyCodeDTO);
}
