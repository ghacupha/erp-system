package io.github.erp.service.mapper;

import io.github.erp.domain.IsoCountryCode;
import io.github.erp.service.dto.IsoCountryCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IsoCountryCode} and its DTO {@link IsoCountryCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface IsoCountryCodeMapper extends EntityMapper<IsoCountryCodeDTO, IsoCountryCode> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    IsoCountryCodeDTO toDto(IsoCountryCode s);

    @Mapping(target = "removePlaceholder", ignore = true)
    IsoCountryCode toEntity(IsoCountryCodeDTO isoCountryCodeDTO);
}
