package io.github.erp.service.mapper;

import io.github.erp.domain.DeliveryNote;
import io.github.erp.service.dto.DeliveryNoteDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryNote} and its DTO {@link DeliveryNoteDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class, DealerMapper.class, BusinessStampMapper.class, PurchaseOrderMapper.class, BusinessDocumentMapper.class,
    }
)
public interface DeliveryNoteMapper extends EntityMapper<DeliveryNoteDTO, DeliveryNote> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "receivedBy", source = "receivedBy", qualifiedByName = "dealerName")
    @Mapping(target = "deliveryStamps", source = "deliveryStamps", qualifiedByName = "detailsSet")
    @Mapping(target = "purchaseOrder", source = "purchaseOrder", qualifiedByName = "purchaseOrderNumber")
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "dealerName")
    @Mapping(target = "signatories", source = "signatories", qualifiedByName = "dealerNameSet")
    @Mapping(target = "otherPurchaseOrders", source = "otherPurchaseOrders", qualifiedByName = "purchaseOrderNumberSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    DeliveryNoteDTO toDto(DeliveryNote s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeDeliveryStamps", ignore = true)
    @Mapping(target = "removeSignatories", ignore = true)
    @Mapping(target = "removeOtherPurchaseOrders", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    DeliveryNote toEntity(DeliveryNoteDTO deliveryNoteDTO);

    @Named("deliveryNoteNumberSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "deliveryNoteNumber", source = "deliveryNoteNumber")
    Set<DeliveryNoteDTO> toDtoDeliveryNoteNumberSet(Set<DeliveryNote> deliveryNote);
}
