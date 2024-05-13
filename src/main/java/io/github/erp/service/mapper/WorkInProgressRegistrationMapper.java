package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.WorkInProgressRegistration;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkInProgressRegistration} and its DTO {@link WorkInProgressRegistrationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        SettlementCurrencyMapper.class,
        WorkProjectRegisterMapper.class,
        BusinessDocumentMapper.class,
        AssetAccessoryMapper.class,
        AssetWarrantyMapper.class,
        PaymentInvoiceMapper.class,
        ServiceOutletMapper.class,
        SettlementMapper.class,
        PurchaseOrderMapper.class,
        DeliveryNoteMapper.class,
        JobSheetMapper.class,
        DealerMapper.class,
    }
)
public interface WorkInProgressRegistrationMapper extends EntityMapper<WorkInProgressRegistrationDTO, WorkInProgressRegistration> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "workInProgressGroup", source = "workInProgressGroup", qualifiedByName = "sequenceNumber")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "workProjectRegister", source = "workProjectRegister", qualifiedByName = "catalogueNumber")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    @Mapping(target = "assetAccessories", source = "assetAccessories", qualifiedByName = "assetDetailsSet")
    @Mapping(target = "assetWarranties", source = "assetWarranties", qualifiedByName = "descriptionSet")
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "invoiceNumber")
    @Mapping(target = "outletCode", source = "outletCode", qualifiedByName = "outletCode")
    @Mapping(target = "settlementTransaction", source = "settlementTransaction", qualifiedByName = "paymentNumber")
    @Mapping(target = "purchaseOrder", source = "purchaseOrder", qualifiedByName = "purchaseOrderNumber")
    @Mapping(target = "deliveryNote", source = "deliveryNote", qualifiedByName = "deliveryNoteNumber")
    @Mapping(target = "jobSheet", source = "jobSheet", qualifiedByName = "serialNumber")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    WorkInProgressRegistrationDTO toDto(WorkInProgressRegistration s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    @Mapping(target = "removeAssetAccessory", ignore = true)
    @Mapping(target = "removeAssetWarranty", ignore = true)
    WorkInProgressRegistration toEntity(WorkInProgressRegistrationDTO workInProgressRegistrationDTO);

    @Named("sequenceNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sequenceNumber", source = "sequenceNumber")
    WorkInProgressRegistrationDTO toDtoSequenceNumber(WorkInProgressRegistration workInProgressRegistration);
}
