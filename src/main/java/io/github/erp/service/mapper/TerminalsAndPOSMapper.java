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
