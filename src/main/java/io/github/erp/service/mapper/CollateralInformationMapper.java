package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.CollateralInformation;
import io.github.erp.service.dto.CollateralInformationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CollateralInformation} and its DTO {@link CollateralInformationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { InstitutionCodeMapper.class, BankBranchCodeMapper.class, CollateralTypeMapper.class, CountySubCountyCodeMapper.class }
)
public interface CollateralInformationMapper extends EntityMapper<CollateralInformationDTO, CollateralInformation> {
    @Mapping(target = "bankCode", source = "bankCode", qualifiedByName = "institutionName")
    @Mapping(target = "branchCode", source = "branchCode", qualifiedByName = "branchCode")
    @Mapping(target = "collateralType", source = "collateralType", qualifiedByName = "collateralType")
    @Mapping(target = "countyCode", source = "countyCode", qualifiedByName = "subCountyName")
    CollateralInformationDTO toDto(CollateralInformation s);
}
