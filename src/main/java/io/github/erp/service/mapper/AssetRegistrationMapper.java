package io.github.erp.service.mapper;

import io.github.erp.domain.AssetRegistration;
import io.github.erp.service.dto.AssetRegistrationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetRegistration} and its DTO {@link AssetRegistrationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        PaymentInvoiceMapper.class,
        ServiceOutletMapper.class,
        SettlementMapper.class,
        AssetCategoryMapper.class,
        PurchaseOrderMapper.class,
        DeliveryNoteMapper.class,
        JobSheetMapper.class,
        DealerMapper.class,
        SettlementCurrencyMapper.class,
    }
)
public interface AssetRegistrationMapper extends EntityMapper<AssetRegistrationDTO, AssetRegistration> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentInvoices", source = "paymentInvoices", qualifiedByName = "invoiceNumberSet")
    @Mapping(target = "serviceOutlets", source = "serviceOutlets", qualifiedByName = "outletCodeSet")
    @Mapping(target = "settlements", source = "settlements", qualifiedByName = "paymentNumberSet")
    @Mapping(target = "assetCategory", source = "assetCategory", qualifiedByName = "assetCategoryName")
    @Mapping(target = "purchaseOrders", source = "purchaseOrders", qualifiedByName = "purchaseOrderNumberSet")
    @Mapping(target = "deliveryNotes", source = "deliveryNotes", qualifiedByName = "deliveryNoteNumberSet")
    @Mapping(target = "jobSheets", source = "jobSheets", qualifiedByName = "serialNumberSet")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    @Mapping(target = "designatedUsers", source = "designatedUsers", qualifiedByName = "dealerNameSet")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    AssetRegistrationDTO toDto(AssetRegistration s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePaymentInvoices", ignore = true)
    @Mapping(target = "removeServiceOutlet", ignore = true)
    @Mapping(target = "removeSettlement", ignore = true)
    @Mapping(target = "removePurchaseOrder", ignore = true)
    @Mapping(target = "removeDeliveryNote", ignore = true)
    @Mapping(target = "removeJobSheet", ignore = true)
    @Mapping(target = "removeDesignatedUsers", ignore = true)
    AssetRegistration toEntity(AssetRegistrationDTO assetRegistrationDTO);
}
