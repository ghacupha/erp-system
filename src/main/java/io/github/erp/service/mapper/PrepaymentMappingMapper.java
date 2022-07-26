package io.github.erp.service.mapper;

import io.github.erp.domain.PrepaymentMapping;
import io.github.erp.service.dto.PrepaymentMappingDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrepaymentMapping} and its DTO {@link PrepaymentMappingDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface PrepaymentMappingMapper extends EntityMapper<PrepaymentMappingDTO, PrepaymentMapping> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    PrepaymentMappingDTO toDto(PrepaymentMapping s);

    @Mapping(target = "removePlaceholder", ignore = true)
    PrepaymentMapping toEntity(PrepaymentMappingDTO prepaymentMappingDTO);

    @Named("keySet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "key", source = "key")
    Set<PrepaymentMappingDTO> toDtoKeySet(Set<PrepaymentMapping> prepaymentMapping);
}
