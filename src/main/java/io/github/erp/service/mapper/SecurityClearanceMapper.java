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
