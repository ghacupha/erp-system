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
