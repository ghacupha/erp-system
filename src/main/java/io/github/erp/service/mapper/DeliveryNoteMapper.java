package io.github.erp.service.mapper;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    @Named("deliveryNoteNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "deliveryNoteNumber", source = "deliveryNoteNumber")
    DeliveryNoteDTO toDtoDeliveryNoteNumber(DeliveryNote deliveryNote);
}
