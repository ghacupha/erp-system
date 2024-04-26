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
import io.github.erp.domain.CreditNote;
import io.github.erp.service.dto.CreditNoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CreditNote} and its DTO {@link CreditNoteDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PurchaseOrderMapper.class,
        PaymentInvoiceMapper.class,
        PaymentLabelMapper.class,
        PlaceholderMapper.class,
        SettlementCurrencyMapper.class,
    }
)
public interface CreditNoteMapper extends EntityMapper<CreditNoteDTO, CreditNote> {
    @Mapping(target = "purchaseOrders", source = "purchaseOrders", qualifiedByName = "purchaseOrderNumberSet")
    @Mapping(target = "invoices", source = "invoices", qualifiedByName = "invoiceNumberSet")
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    CreditNoteDTO toDto(CreditNote s);

    @Mapping(target = "removePurchaseOrders", ignore = true)
    @Mapping(target = "removeInvoices", ignore = true)
    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    CreditNote toEntity(CreditNoteDTO creditNoteDTO);
}
