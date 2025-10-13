package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.TerminalsAndPOS;
import io.github.erp.service.dto.TerminalsAndPOSDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TerminalsAndPOS} and its DTO {@link TerminalsAndPOSDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        TerminalTypesMapper.class,
        TerminalFunctionsMapper.class,
        CountySubCountyCodeMapper.class,
        InstitutionCodeMapper.class,
        BankBranchCodeMapper.class,
    }
)
public interface TerminalsAndPOSMapper extends EntityMapper<TerminalsAndPOSDTO, TerminalsAndPOS> {
    @Mapping(target = "terminalType", source = "terminalType", qualifiedByName = "txnTerminalTypeCode")
    @Mapping(target = "terminalFunctionality", source = "terminalFunctionality", qualifiedByName = "terminalFunctionality")
    @Mapping(target = "physicalLocation", source = "physicalLocation", qualifiedByName = "subCountyCode")
    @Mapping(target = "bankId", source = "bankId", qualifiedByName = "institutionName")
    @Mapping(target = "branchId", source = "branchId", qualifiedByName = "branchCode")
    TerminalsAndPOSDTO toDto(TerminalsAndPOS s);
}
