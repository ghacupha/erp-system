package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
