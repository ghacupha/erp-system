package io.github.erp.service.mapper;

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
