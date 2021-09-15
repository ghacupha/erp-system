package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.TaxReferenceDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxReference} and its DTO {@link TaxReferenceDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface TaxReferenceMapper extends EntityMapper<TaxReferenceDTO, TaxReference> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    TaxReferenceDTO toDto(TaxReference s);

    @Mapping(target = "removePlaceholder", ignore = true)
    TaxReference toEntity(TaxReferenceDTO taxReferenceDTO);
}
