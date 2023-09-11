package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
