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
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.service.dto.ServiceOutletDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceOutlet} and its DTO {@link ServiceOutletDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PlaceholderMapper.class, BankBranchCodeMapper.class, OutletTypeMapper.class, OutletStatusMapper.class, CountyCodeMapper.class }
)
public interface ServiceOutletMapper extends EntityMapper<ServiceOutletDTO, ServiceOutlet> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "bankCode", source = "bankCode", qualifiedByName = "branchCode")
    @Mapping(target = "outletType", source = "outletType", qualifiedByName = "outletType")
    @Mapping(target = "outletStatus", source = "outletStatus", qualifiedByName = "branchStatusType")
    @Mapping(target = "countyName", source = "countyName", qualifiedByName = "countyName")
    @Mapping(target = "subCountyName", source = "subCountyName", qualifiedByName = "subCountyName")
    ServiceOutletDTO toDto(ServiceOutlet s);

    @Mapping(target = "removePlaceholder", ignore = true)
    ServiceOutlet toEntity(ServiceOutletDTO serviceOutletDTO);

    @Named("outletCodeSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "outletCode", source = "outletCode")
    Set<ServiceOutletDTO> toDtoOutletCodeSet(Set<ServiceOutlet> serviceOutlet);

    @Named("outletCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "outletCode", source = "outletCode")
    ServiceOutletDTO toDtoOutletCode(ServiceOutlet serviceOutlet);
}
