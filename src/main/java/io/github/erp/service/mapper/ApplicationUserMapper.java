package io.github.erp.service.mapper;

import io.github.erp.domain.ApplicationUser;
import io.github.erp.service.dto.ApplicationUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DealerMapper.class, SecurityClearanceMapper.class, UserMapper.class, UniversallyUniqueMappingMapper.class }
)
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {
    @Mapping(target = "organization", source = "organization", qualifiedByName = "dealerName")
    @Mapping(target = "department", source = "department", qualifiedByName = "dealerName")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "clearanceLevel")
    @Mapping(target = "systemIdentity", source = "systemIdentity", qualifiedByName = "login")
    @Mapping(target = "userProperties", source = "userProperties", qualifiedByName = "mappedValueSet")
    ApplicationUserDTO toDto(ApplicationUser s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationUserDTO toDtoId(ApplicationUser applicationUser);

    @Mapping(target = "removeUserProperties", ignore = true)
    ApplicationUser toEntity(ApplicationUserDTO applicationUserDTO);
}
