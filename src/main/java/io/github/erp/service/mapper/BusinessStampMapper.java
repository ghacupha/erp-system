package io.github.erp.service.mapper;

import io.github.erp.domain.BusinessStamp;
import io.github.erp.service.dto.BusinessStampDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessStamp} and its DTO {@link BusinessStampDTO}.
 */
@Mapper(componentModel = "spring", uses = { DealerMapper.class, PlaceholderMapper.class })
public interface BusinessStampMapper extends EntityMapper<BusinessStampDTO, BusinessStamp> {
    @Mapping(target = "stampHolder", source = "stampHolder", qualifiedByName = "dealerName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    BusinessStampDTO toDto(BusinessStamp s);

    @Mapping(target = "removePlaceholder", ignore = true)
    BusinessStamp toEntity(BusinessStampDTO businessStampDTO);

    @Named("detailsSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "details", source = "details")
    Set<BusinessStampDTO> toDtoDetailsSet(Set<BusinessStamp> businessStamp);
}
