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
import io.github.erp.domain.PaymentInvoice;
import io.github.erp.service.dto.PaymentInvoiceDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentInvoice} and its DTO {@link PaymentInvoiceDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PurchaseOrderMapper.class,
        PlaceholderMapper.class,
        PaymentLabelMapper.class,
        SettlementCurrencyMapper.class,
        DealerMapper.class,
        DeliveryNoteMapper.class,
        JobSheetMapper.class,
        BusinessDocumentMapper.class,
    }
)
public interface PaymentInvoiceMapper extends EntityMapper<PaymentInvoiceDTO, PaymentInvoice> {
    @Mapping(target = "purchaseOrders", source = "purchaseOrders", qualifiedByName = "purchaseOrderNumberSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "biller", source = "biller", qualifiedByName = "dealerName")
    @Mapping(target = "deliveryNotes", source = "deliveryNotes", qualifiedByName = "deliveryNoteNumberSet")
    @Mapping(target = "jobSheets", source = "jobSheets", qualifiedByName = "serialNumberSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    PaymentInvoiceDTO toDto(PaymentInvoice s);

    @Mapping(target = "removePurchaseOrder", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removeDeliveryNote", ignore = true)
    @Mapping(target = "removeJobSheet", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    PaymentInvoice toEntity(PaymentInvoiceDTO paymentInvoiceDTO);

    @Named("invoiceNumberSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "invoiceNumber", source = "invoiceNumber")
    Set<PaymentInvoiceDTO> toDtoInvoiceNumberSet(Set<PaymentInvoice> paymentInvoice);

    @Named("invoiceNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "invoiceNumber", source = "invoiceNumber")
    PaymentInvoiceDTO toDtoInvoiceNumber(PaymentInvoice paymentInvoice);
}
