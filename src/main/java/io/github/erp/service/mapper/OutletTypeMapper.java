package io.github.erp.service.mapper;

import io.github.erp.domain.OutletType;
import io.github.erp.service.dto.OutletTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OutletType} and its DTO {@link OutletTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface OutletTypeMapper extends EntityMapper<OutletTypeDTO, OutletType> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    OutletTypeDTO toDto(OutletType s);

    @Mapping(target = "removePlaceholder", ignore = true)
    OutletType toEntity(OutletTypeDTO outletTypeDTO);

    @Named("outletType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "outletType", source = "outletType")
    OutletTypeDTO toDtoOutletType(OutletType outletType);
}
