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
