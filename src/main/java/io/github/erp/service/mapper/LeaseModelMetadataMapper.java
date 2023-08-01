package io.github.erp.service.mapper;

import io.github.erp.domain.LeaseModelMetadata;
import io.github.erp.service.dto.LeaseModelMetadataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaseModelMetadata} and its DTO {@link LeaseModelMetadataDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        UniversallyUniqueMappingMapper.class,
        LeaseContractMapper.class,
        SettlementCurrencyMapper.class,
        BusinessDocumentMapper.class,
        SecurityClearanceMapper.class,
        TransactionAccountMapper.class,
    }
)
public interface LeaseModelMetadataMapper extends EntityMapper<LeaseModelMetadataDTO, LeaseModelMetadata> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "leaseMappings", source = "leaseMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "leaseContract", source = "leaseContract", qualifiedByName = "bookingId")
    @Mapping(target = "predecessor", source = "predecessor", qualifiedByName = "modelTitle")
    @Mapping(target = "liabilityCurrency", source = "liabilityCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "rouAssetCurrency", source = "rouAssetCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "modelAttachments", source = "modelAttachments", qualifiedByName = "documentTitle")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "clearanceLevel")
    @Mapping(target = "leaseLiabilityAccount", source = "leaseLiabilityAccount", qualifiedByName = "accountNumber")
    @Mapping(target = "interestPayableAccount", source = "interestPayableAccount", qualifiedByName = "accountNumber")
    @Mapping(target = "interestExpenseAccount", source = "interestExpenseAccount", qualifiedByName = "accountNumber")
    @Mapping(target = "rouAssetAccount", source = "rouAssetAccount", qualifiedByName = "accountNumber")
    @Mapping(target = "rouDepreciationAccount", source = "rouDepreciationAccount", qualifiedByName = "accountNumber")
    @Mapping(target = "accruedDepreciationAccount", source = "accruedDepreciationAccount", qualifiedByName = "accountNumber")
    LeaseModelMetadataDTO toDto(LeaseModelMetadata s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeLeaseMapping", ignore = true)
    LeaseModelMetadata toEntity(LeaseModelMetadataDTO leaseModelMetadataDTO);

    @Named("modelTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "modelTitle", source = "modelTitle")
    LeaseModelMetadataDTO toDtoModelTitle(LeaseModelMetadata leaseModelMetadata);
}
