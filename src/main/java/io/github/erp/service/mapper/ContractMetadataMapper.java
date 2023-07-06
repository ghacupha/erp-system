package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.1
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
