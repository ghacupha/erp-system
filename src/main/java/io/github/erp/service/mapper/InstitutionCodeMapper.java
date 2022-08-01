package io.github.erp.service.mapper;

import io.github.erp.domain.InstitutionCode;
import io.github.erp.service.dto.InstitutionCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InstitutionCode} and its DTO {@link InstitutionCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface InstitutionCodeMapper extends EntityMapper<InstitutionCodeDTO, InstitutionCode> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    InstitutionCodeDTO toDto(InstitutionCode s);

    @Mapping(target = "removePlaceholder", ignore = true)
    InstitutionCode toEntity(InstitutionCodeDTO institutionCodeDTO);
}
