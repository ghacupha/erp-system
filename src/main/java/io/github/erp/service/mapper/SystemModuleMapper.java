package io.github.erp.service.mapper;

import io.github.erp.domain.SystemModule;
import io.github.erp.service.dto.SystemModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemModule} and its DTO {@link SystemModuleDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface SystemModuleMapper extends EntityMapper<SystemModuleDTO, SystemModule> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "applicationMappings", source = "applicationMappings", qualifiedByName = "universalKeySet")
    SystemModuleDTO toDto(SystemModule s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeApplicationMapping", ignore = true)
    SystemModule toEntity(SystemModuleDTO systemModuleDTO);

    @Named("moduleName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "moduleName", source = "moduleName")
    SystemModuleDTO toDtoModuleName(SystemModule systemModule);
}
