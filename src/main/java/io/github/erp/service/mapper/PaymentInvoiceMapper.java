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
