package io.github.erp.service.mapper;

import io.github.erp.domain.AssetAccessory;
import io.github.erp.service.dto.AssetAccessoryDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetAccessory} and its DTO {@link AssetAccessoryDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        AssetRegistrationMapper.class,
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
        BusinessDocumentMapper.class,
        UniversallyUniqueMappingMapper.class,
    }
)
public interface AssetAccessoryMapper extends EntityMapper<AssetAccessoryDTO, AssetAccessory> {
    @Mapping(target = "assetRegistration", source = "assetRegistration", qualifiedByName = "assetNumber")
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
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    AssetAccessoryDTO toDto(AssetAccessory s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePaymentInvoices", ignore = true)
    @Mapping(target = "removeServiceOutlet", ignore = true)
    @Mapping(target = "removeSettlement", ignore = true)
    @Mapping(target = "removePurchaseOrder", ignore = true)
    @Mapping(target = "removeDeliveryNote", ignore = true)
    @Mapping(target = "removeJobSheet", ignore = true)
    @Mapping(target = "removeDesignatedUsers", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    AssetAccessory toEntity(AssetAccessoryDTO assetAccessoryDTO);

    @Named("assetDetailsSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "assetDetails", source = "assetDetails")
    Set<AssetAccessoryDTO> toDtoAssetDetailsSet(Set<AssetAccessory> assetAccessory);
}
