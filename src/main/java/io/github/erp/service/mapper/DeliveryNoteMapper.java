package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

    @Named("deliveryNoteNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "deliveryNoteNumber", source = "deliveryNoteNumber")
    DeliveryNoteDTO toDtoDeliveryNoteNumber(DeliveryNote deliveryNote);

    @Named("deliveryNoteNumberSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "deliveryNoteNumber", source = "deliveryNoteNumber")
    Set<DeliveryNoteDTO> toDtoDeliveryNoteNumberSet(Set<DeliveryNote> deliveryNote);
}
