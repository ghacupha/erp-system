package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.SettlementRequisition;
import io.github.erp.service.dto.SettlementRequisitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SettlementRequisition} and its DTO {@link SettlementRequisitionDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        SettlementCurrencyMapper.class,
        ApplicationUserMapper.class,
        DealerMapper.class,
        PaymentInvoiceMapper.class,
        DeliveryNoteMapper.class,
        JobSheetMapper.class,
        BusinessDocumentMapper.class,
        UniversallyUniqueMappingMapper.class,
        PlaceholderMapper.class,
        SettlementMapper.class,
    }
)
public interface SettlementRequisitionMapper extends EntityMapper<SettlementRequisitionDTO, SettlementRequisition> {
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "currentOwner", source = "currentOwner", qualifiedByName = "applicationIdentity")
    @Mapping(target = "nativeOwner", source = "nativeOwner", qualifiedByName = "applicationIdentity")
    @Mapping(target = "nativeDepartment", source = "nativeDepartment", qualifiedByName = "dealerName")
    @Mapping(target = "biller", source = "biller", qualifiedByName = "dealerName")
    @Mapping(target = "paymentInvoices", source = "paymentInvoices", qualifiedByName = "invoiceNumberSet")
    @Mapping(target = "deliveryNotes", source = "deliveryNotes", qualifiedByName = "deliveryNoteNumberSet")
    @Mapping(target = "jobSheets", source = "jobSheets", qualifiedByName = "serialNumberSet")
    @Mapping(target = "signatures", source = "signatures", qualifiedByName = "dealerNameSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    @Mapping(target = "applicationMappings", source = "applicationMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "settlements", source = "settlements", qualifiedByName = "paymentNumberSet")
    SettlementRequisitionDTO toDto(SettlementRequisition s);

    @Mapping(target = "removePaymentInvoice", ignore = true)
    @Mapping(target = "removeDeliveryNote", ignore = true)
    @Mapping(target = "removeJobSheet", ignore = true)
    @Mapping(target = "removeSignatures", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    @Mapping(target = "removeApplicationMapping", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeSettlement", ignore = true)
    SettlementRequisition toEntity(SettlementRequisitionDTO settlementRequisitionDTO);
}
