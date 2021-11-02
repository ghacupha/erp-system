package io.github.erp.service.mapper;

import io.github.erp.domain.Placeholder;
import io.github.erp.service.dto.PlaceholderDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Placeholder} and its DTO {@link PlaceholderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlaceholderMapper extends EntityMapper<PlaceholderDTO, Placeholder> {
    @Mapping(target = "containingPlaceholder", source = "containingPlaceholder", qualifiedByName = "description")
    PlaceholderDTO toDto(Placeholder s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<PlaceholderDTO> toDtoIdSet(Set<Placeholder> placeholder);

    @Named("descriptionSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    Set<PlaceholderDTO> toDtoDescriptionSet(Set<Placeholder> placeholder);

    @Named("description")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    PlaceholderDTO toDtoDescription(Placeholder placeholder);
}
