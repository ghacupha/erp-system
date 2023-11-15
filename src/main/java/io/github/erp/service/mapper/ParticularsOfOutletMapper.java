package io.github.erp.service.mapper;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.ParticularsOfOutlet;
import io.github.erp.service.dto.ParticularsOfOutletDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ParticularsOfOutlet} and its DTO {@link ParticularsOfOutletDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        CountySubCountyCodeMapper.class,
        InstitutionCodeMapper.class,
        BankBranchCodeMapper.class,
        OutletTypeMapper.class,
        OutletStatusMapper.class,
    }
)
public interface ParticularsOfOutletMapper extends EntityMapper<ParticularsOfOutletDTO, ParticularsOfOutlet> {
    @Mapping(target = "subCountyCode", source = "subCountyCode", qualifiedByName = "subCountyName")
    @Mapping(target = "bankCode", source = "bankCode", qualifiedByName = "institutionName")
    @Mapping(target = "outletId", source = "outletId", qualifiedByName = "branchCode")
    @Mapping(target = "typeOfOutlet", source = "typeOfOutlet", qualifiedByName = "outletType")
    @Mapping(target = "outletStatus", source = "outletStatus", qualifiedByName = "branchStatusType")
    ParticularsOfOutletDTO toDto(ParticularsOfOutlet s);
}
