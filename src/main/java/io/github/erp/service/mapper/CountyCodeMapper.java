package io.github.erp.service.mapper;

import io.github.erp.domain.CountyCode;
import io.github.erp.service.dto.CountyCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CountyCode} and its DTO {@link CountyCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface CountyCodeMapper extends EntityMapper<CountyCodeDTO, CountyCode> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    CountyCodeDTO toDto(CountyCode s);

    @Mapping(target = "removePlaceholder", ignore = true)
    CountyCode toEntity(CountyCodeDTO countyCodeDTO);

    @Named("countyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "countyName", source = "countyName")
    CountyCodeDTO toDtoCountyName(CountyCode countyCode);

    @Named("subCountyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "subCountyName", source = "subCountyName")
    CountyCodeDTO toDtoSubCountyName(CountyCode countyCode);
}
