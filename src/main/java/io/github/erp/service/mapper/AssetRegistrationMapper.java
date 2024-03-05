package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.AssetRegistration;
import io.github.erp.service.dto.AssetRegistrationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetRegistration} and its DTO {@link AssetRegistrationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        PaymentInvoiceMapper.class,
        ServiceOutletMapper.class,
        SettlementMapper.class,
        AssetCategoryMapper.class,
        PurchaseOrderMapper.class,
        DeliveryNoteMapper.class,
        JobSheetMapper.class,
        DealerMapper.class,
        SettlementCurrencyMapper.class,
        BusinessDocumentMapper.class,
        AssetWarrantyMapper.class,
        UniversallyUniqueMappingMapper.class,
        AssetAccessoryMapper.class,
    }
)
public interface AssetRegistrationMapper extends EntityMapper<AssetRegistrationDTO, AssetRegistration> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentInvoices", source = "paymentInvoices", qualifiedByName = "invoiceNumberSet")
    @Mapping(target = "otherRelatedServiceOutlets", source = "otherRelatedServiceOutlets", qualifiedByName = "outletCodeSet")
    @Mapping(target = "otherRelatedSettlements", source = "otherRelatedSettlements", qualifiedByName = "paymentNumberSet")
    @Mapping(target = "assetCategory", source = "assetCategory", qualifiedByName = "assetCategoryName")
    @Mapping(target = "purchaseOrders", source = "purchaseOrders", qualifiedByName = "purchaseOrderNumberSet")
    @Mapping(target = "deliveryNotes", source = "deliveryNotes", qualifiedByName = "deliveryNoteNumberSet")
    @Mapping(target = "jobSheets", source = "jobSheets", qualifiedByName = "serialNumberSet")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    @Mapping(target = "designatedUsers", source = "designatedUsers", qualifiedByName = "dealerNameSet")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    @Mapping(target = "assetWarranties", source = "assetWarranties", qualifiedByName = "descriptionSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "assetAccessories", source = "assetAccessories", qualifiedByName = "assetDetailsSet")
    @Mapping(target = "mainServiceOutlet", source = "mainServiceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "acquiringTransaction", source = "acquiringTransaction", qualifiedByName = "paymentNumber")
    AssetRegistrationDTO toDto(AssetRegistration s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssetRegistrationDTO toDtoId(AssetRegistration assetRegistration);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePaymentInvoices", ignore = true)
    @Mapping(target = "removeOtherRelatedServiceOutlets", ignore = true)
    @Mapping(target = "removeOtherRelatedSettlements", ignore = true)
    @Mapping(target = "removePurchaseOrder", ignore = true)
    @Mapping(target = "removeDeliveryNote", ignore = true)
    @Mapping(target = "removeJobSheet", ignore = true)
    @Mapping(target = "removeDesignatedUsers", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    @Mapping(target = "removeAssetWarranty", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    @Mapping(target = "removeAssetAccessory", ignore = true)
    AssetRegistration toEntity(AssetRegistrationDTO assetRegistrationDTO);

    @Named("assetNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "assetNumber", source = "assetNumber")
    AssetRegistrationDTO toDtoAssetNumber(AssetRegistration assetRegistration);
}
