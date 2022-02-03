package io.github.erp.service.mapper;

import io.github.erp.domain.PaymentInvoice;
import io.github.erp.service.dto.PaymentInvoiceDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentInvoice} and its DTO {@link PaymentInvoiceDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PurchaseOrderMapper.class, PlaceholderMapper.class, PaymentLabelMapper.class, SettlementCurrencyMapper.class }
)
public interface PaymentInvoiceMapper extends EntityMapper<PaymentInvoiceDTO, PaymentInvoice> {
    @Mapping(target = "purchaseOrders", source = "purchaseOrders", qualifiedByName = "purchaseOrderNumberSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    PaymentInvoiceDTO toDto(PaymentInvoice s);

    @Mapping(target = "removePurchaseOrder", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePaymentLabel", ignore = true)
    PaymentInvoice toEntity(PaymentInvoiceDTO paymentInvoiceDTO);

    @Named("invoiceNumberSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "invoiceNumber", source = "invoiceNumber")
    Set<PaymentInvoiceDTO> toDtoInvoiceNumberSet(Set<PaymentInvoice> paymentInvoice);
}
