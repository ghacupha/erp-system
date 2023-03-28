package io.github.erp.service.mapper;

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
