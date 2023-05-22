package io.github.erp.service.mapper;

import io.github.erp.domain.PurchaseOrder;
import io.github.erp.service.dto.PurchaseOrderDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseOrder} and its DTO {@link PurchaseOrderDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { SettlementCurrencyMapper.class, PlaceholderMapper.class, DealerMapper.class, BusinessDocumentMapper.class }
)
public interface PurchaseOrderMapper extends EntityMapper<PurchaseOrderDTO, PurchaseOrder> {
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "signatories", source = "signatories", qualifiedByName = "dealerNameSet")
    @Mapping(target = "vendor", source = "vendor", qualifiedByName = "dealerName")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    PurchaseOrderDTO toDto(PurchaseOrder s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeSignatories", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    PurchaseOrder toEntity(PurchaseOrderDTO purchaseOrderDTO);

    @Named("purchaseOrderNumberSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "purchaseOrderNumber", source = "purchaseOrderNumber")
    Set<PurchaseOrderDTO> toDtoPurchaseOrderNumberSet(Set<PurchaseOrder> purchaseOrder);

    @Named("purchaseOrderNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "purchaseOrderNumber", source = "purchaseOrderNumber")
    PurchaseOrderDTO toDtoPurchaseOrderNumber(PurchaseOrder purchaseOrder);
}
