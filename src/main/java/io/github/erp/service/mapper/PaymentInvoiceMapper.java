package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.8.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
}
