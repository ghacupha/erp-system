package io.github.erp.service.mapper;

import io.github.erp.domain.PrepaymentMarshalling;
import io.github.erp.service.dto.PrepaymentMarshallingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrepaymentMarshalling} and its DTO {@link PrepaymentMarshallingDTO}.
 */
@Mapper(componentModel = "spring", uses = { PrepaymentAccountMapper.class, PlaceholderMapper.class })
public interface PrepaymentMarshallingMapper extends EntityMapper<PrepaymentMarshallingDTO, PrepaymentMarshalling> {
    @Mapping(target = "prepaymentAccount", source = "prepaymentAccount", qualifiedByName = "catalogueNumber")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    PrepaymentMarshallingDTO toDto(PrepaymentMarshalling s);

    @Mapping(target = "removePlaceholder", ignore = true)
    PrepaymentMarshalling toEntity(PrepaymentMarshallingDTO prepaymentMarshallingDTO);
}
