package io.github.erp.service.mapper;

import io.github.erp.domain.ApplicationUser;
import io.github.erp.service.dto.ApplicationUserDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        DealerMapper.class, SecurityClearanceMapper.class, UserMapper.class, UniversallyUniqueMappingMapper.class, PlaceholderMapper.class,
    }
)
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {
    @Mapping(target = "organization", source = "organization", qualifiedByName = "dealerName")
    @Mapping(target = "department", source = "department", qualifiedByName = "dealerName")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "clearanceLevel")
    @Mapping(target = "systemIdentity", source = "systemIdentity", qualifiedByName = "login")
    @Mapping(target = "userProperties", source = "userProperties", qualifiedByName = "mappedValueSet")
    @Mapping(target = "dealerIdentity", source = "dealerIdentity", qualifiedByName = "dealerName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    ApplicationUserDTO toDto(ApplicationUser s);

    @Mapping(target = "removeUserProperties", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    ApplicationUser toEntity(ApplicationUserDTO applicationUserDTO);

    @Named("applicationIdentity")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "applicationIdentity", source = "applicationIdentity")
    ApplicationUserDTO toDtoApplicationIdentity(ApplicationUser applicationUser);

    @Named("applicationIdentitySet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "applicationIdentity", source = "applicationIdentity")
    Set<ApplicationUserDTO> toDtoApplicationIdentitySet(Set<ApplicationUser> applicationUser);

    @Named("designation")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designation", source = "designation")
    ApplicationUserDTO toDtoDesignation(ApplicationUser applicationUser);
}
