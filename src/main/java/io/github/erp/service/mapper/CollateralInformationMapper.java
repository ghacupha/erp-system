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
