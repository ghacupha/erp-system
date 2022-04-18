package io.github.erp.service.mapper;

import io.github.erp.domain.WorkInProgressRegistration;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkInProgressRegistration} and its DTO {@link WorkInProgressRegistrationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        PaymentInvoiceMapper.class,
        ServiceOutletMapper.class,
        SettlementMapper.class,
        PurchaseOrderMapper.class,
        DeliveryNoteMapper.class,
        JobSheetMapper.class,
        DealerMapper.class,
    }
)
public interface WorkInProgressRegistrationMapper extends EntityMapper<WorkInProgressRegistrationDTO, WorkInProgressRegistration> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentInvoices", source = "paymentInvoices", qualifiedByName = "invoiceNumberSet")
    @Mapping(target = "serviceOutlets", source = "serviceOutlets", qualifiedByName = "outletCodeSet")
    @Mapping(target = "settlements", source = "settlements", qualifiedByName = "paymentNumberSet")
    @Mapping(target = "purchaseOrders", source = "purchaseOrders", qualifiedByName = "purchaseOrderNumberSet")
    @Mapping(target = "deliveryNotes", source = "deliveryNotes", qualifiedByName = "deliveryNoteNumberSet")
    @Mapping(target = "jobSheets", source = "jobSheets", qualifiedByName = "serialNumberSet")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    WorkInProgressRegistrationDTO toDto(WorkInProgressRegistration s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<WorkInProgressRegistrationDTO> toDtoIdSet(Set<WorkInProgressRegistration> workInProgressRegistration);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePaymentInvoices", ignore = true)
    @Mapping(target = "removeServiceOutlet", ignore = true)
    @Mapping(target = "removeSettlement", ignore = true)
    @Mapping(target = "removePurchaseOrder", ignore = true)
    @Mapping(target = "removeDeliveryNote", ignore = true)
    @Mapping(target = "removeJobSheet", ignore = true)
    WorkInProgressRegistration toEntity(WorkInProgressRegistrationDTO workInProgressRegistrationDTO);
}
