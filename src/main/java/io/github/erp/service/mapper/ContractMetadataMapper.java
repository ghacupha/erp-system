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
import io.github.erp.domain.ContractMetadata;
import io.github.erp.service.dto.ContractMetadataDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContractMetadata} and its DTO {@link ContractMetadataDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        DealerMapper.class,
        ApplicationUserMapper.class,
        SecurityClearanceMapper.class,
        PlaceholderMapper.class,
        BusinessDocumentMapper.class,
        UniversallyUniqueMappingMapper.class,
    }
)
public interface ContractMetadataMapper extends EntityMapper<ContractMetadataDTO, ContractMetadata> {
    @Mapping(target = "relatedContracts", source = "relatedContracts", qualifiedByName = "descriptionSet")
    @Mapping(target = "department", source = "department", qualifiedByName = "dealerName")
    @Mapping(target = "contractPartner", source = "contractPartner", qualifiedByName = "dealerName")
    @Mapping(target = "responsiblePerson", source = "responsiblePerson", qualifiedByName = "applicationIdentity")
    @Mapping(target = "signatories", source = "signatories", qualifiedByName = "applicationIdentitySet")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "clearanceLevel")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "contractDocumentFiles", source = "contractDocumentFiles", qualifiedByName = "documentTitleSet")
    @Mapping(target = "contractMappings", source = "contractMappings", qualifiedByName = "universalKeySet")
    ContractMetadataDTO toDto(ContractMetadata s);

    @Mapping(target = "removeRelatedContracts", ignore = true)
    @Mapping(target = "removeSignatory", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeContractDocumentFile", ignore = true)
    @Mapping(target = "removeContractMappings", ignore = true)
    ContractMetadata toEntity(ContractMetadataDTO contractMetadataDTO);

    @Named("contractTitleSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "contractTitle", source = "contractTitle")
    Set<ContractMetadataDTO> toDtoContractTitleSet(Set<ContractMetadata> contractMetadata);

    @Named("descriptionSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    Set<ContractMetadataDTO> toDtoDescriptionSet(Set<ContractMetadata> contractMetadata);
}
