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
import io.github.erp.domain.RelatedPartyRelationship;
import io.github.erp.service.dto.RelatedPartyRelationshipDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RelatedPartyRelationship} and its DTO {@link RelatedPartyRelationshipDTO}.
 */
@Mapper(componentModel = "spring", uses = { InstitutionCodeMapper.class, BankBranchCodeMapper.class, PartyRelationTypeMapper.class })
public interface RelatedPartyRelationshipMapper extends EntityMapper<RelatedPartyRelationshipDTO, RelatedPartyRelationship> {
    @Mapping(target = "bankCode", source = "bankCode", qualifiedByName = "institutionName")
    @Mapping(target = "branchId", source = "branchId", qualifiedByName = "branchCode")
    @Mapping(target = "relationshipType", source = "relationshipType", qualifiedByName = "partyRelationType")
    RelatedPartyRelationshipDTO toDto(RelatedPartyRelationship s);
}
