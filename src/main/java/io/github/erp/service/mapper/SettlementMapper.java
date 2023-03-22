package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.9.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.Settlement;
import io.github.erp.service.dto.SettlementDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Settlement} and its DTO {@link SettlementDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        SettlementCurrencyMapper.class,
        PaymentLabelMapper.class,
        PaymentCategoryMapper.class,
        DealerMapper.class,
        PaymentInvoiceMapper.class,
        BusinessDocumentMapper.class,
    }
)
public interface SettlementMapper extends EntityMapper<SettlementDTO, Settlement> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "categoryName")
    @Mapping(target = "groupSettlement", source = "groupSettlement", qualifiedByName = "id")
    @Mapping(target = "biller", source = "biller", qualifiedByName = "dealerName")
    @Mapping(target = "paymentInvoices", source = "paymentInvoices", qualifiedByName = "invoiceNumberSet")
    @Mapping(target = "signatories", source = "signatories", qualifiedByName = "dealerNameSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    SettlementDTO toDto(Settlement s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SettlementDTO toDtoId(Settlement settlement);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePaymentInvoice", ignore = true)
    @Mapping(target = "removeSignatories", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    Settlement toEntity(SettlementDTO settlementDTO);

    @Named("paymentNumberSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "paymentNumber", source = "paymentNumber")
    Set<SettlementDTO> toDtoPaymentNumberSet(Set<Settlement> settlement);

    @Named("paymentNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "paymentNumber", source = "paymentNumber")
    SettlementDTO toDtoPaymentNumber(Settlement settlement);
}
