package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PlaceholderDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Placeholder} and its DTO {@link PlaceholderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlaceholderMapper extends EntityMapper<PlaceholderDTO, Placeholder> {
    @Mapping(target = "containingPlaceholder", source = "containingPlaceholder", qualifiedByName = "id")
    PlaceholderDTO toDto(Placeholder s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlaceholderDTO toDtoId(Placeholder placeholder);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<PlaceholderDTO> toDtoIdSet(Set<Placeholder> placeholder);
}
