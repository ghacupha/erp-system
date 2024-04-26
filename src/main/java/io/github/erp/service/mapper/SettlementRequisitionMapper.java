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
import io.github.erp.domain.SettlementRequisition;
import io.github.erp.service.dto.SettlementRequisitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SettlementRequisition} and its DTO {@link SettlementRequisitionDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        SettlementCurrencyMapper.class,
        ApplicationUserMapper.class,
        DealerMapper.class,
        PaymentInvoiceMapper.class,
        DeliveryNoteMapper.class,
        JobSheetMapper.class,
        BusinessDocumentMapper.class,
        UniversallyUniqueMappingMapper.class,
        PlaceholderMapper.class,
        SettlementMapper.class,
    }
)
public interface SettlementRequisitionMapper extends EntityMapper<SettlementRequisitionDTO, SettlementRequisition> {
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "currentOwner", source = "currentOwner", qualifiedByName = "applicationIdentity")
    @Mapping(target = "nativeOwner", source = "nativeOwner", qualifiedByName = "applicationIdentity")
    @Mapping(target = "nativeDepartment", source = "nativeDepartment", qualifiedByName = "dealerName")
    @Mapping(target = "biller", source = "biller", qualifiedByName = "dealerName")
    @Mapping(target = "paymentInvoices", source = "paymentInvoices", qualifiedByName = "invoiceNumberSet")
    @Mapping(target = "deliveryNotes", source = "deliveryNotes", qualifiedByName = "deliveryNoteNumberSet")
    @Mapping(target = "jobSheets", source = "jobSheets", qualifiedByName = "serialNumberSet")
    @Mapping(target = "signatures", source = "signatures", qualifiedByName = "dealerNameSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    @Mapping(target = "applicationMappings", source = "applicationMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "settlements", source = "settlements", qualifiedByName = "paymentNumberSet")
    SettlementRequisitionDTO toDto(SettlementRequisition s);

    @Mapping(target = "removePaymentInvoice", ignore = true)
    @Mapping(target = "removeDeliveryNote", ignore = true)
    @Mapping(target = "removeJobSheet", ignore = true)
    @Mapping(target = "removeSignatures", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    @Mapping(target = "removeApplicationMapping", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeSettlement", ignore = true)
    SettlementRequisition toEntity(SettlementRequisitionDTO settlementRequisitionDTO);
}
