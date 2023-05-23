package io.github.erp.service.mapper;

import io.github.erp.domain.SecurityClearance;
import io.github.erp.service.dto.SecurityClearanceDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SecurityClearance} and its DTO {@link SecurityClearanceDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface SecurityClearanceMapper extends EntityMapper<SecurityClearanceDTO, SecurityClearance> {
    @Mapping(target = "grantedClearances", source = "grantedClearances", qualifiedByName = "clearanceLevelSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "systemParameters", source = "systemParameters", qualifiedByName = "universalKeySet")
    SecurityClearanceDTO toDto(SecurityClearance s);

    @Mapping(target = "removeGrantedClearances", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeSystemParameters", ignore = true)
    SecurityClearance toEntity(SecurityClearanceDTO securityClearanceDTO);

    @Named("clearanceLevel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "clearanceLevel", source = "clearanceLevel")
    SecurityClearanceDTO toDtoClearanceLevel(SecurityClearance securityClearance);

    @Named("clearanceLevelSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "clearanceLevel", source = "clearanceLevel")
    Set<SecurityClearanceDTO> toDtoClearanceLevelSet(Set<SecurityClearance> securityClearance);
}
