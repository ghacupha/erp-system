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
