package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
