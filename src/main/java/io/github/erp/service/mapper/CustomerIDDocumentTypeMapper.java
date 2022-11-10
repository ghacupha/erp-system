package io.github.erp.service.mapper;

import io.github.erp.domain.CustomerIDDocumentType;
import io.github.erp.service.dto.CustomerIDDocumentTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerIDDocumentType} and its DTO {@link CustomerIDDocumentTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface CustomerIDDocumentTypeMapper extends EntityMapper<CustomerIDDocumentTypeDTO, CustomerIDDocumentType> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    CustomerIDDocumentTypeDTO toDto(CustomerIDDocumentType s);

    @Mapping(target = "removePlaceholder", ignore = true)
    CustomerIDDocumentType toEntity(CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO);
}
