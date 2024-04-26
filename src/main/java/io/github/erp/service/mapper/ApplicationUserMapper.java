package io.github.erp.service.mapper;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
