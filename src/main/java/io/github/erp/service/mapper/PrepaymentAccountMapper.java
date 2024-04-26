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
import io.github.erp.domain.PrepaymentAccount;
import io.github.erp.service.dto.PrepaymentAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrepaymentAccount} and its DTO {@link PrepaymentAccountDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        SettlementCurrencyMapper.class,
        SettlementMapper.class,
        ServiceOutletMapper.class,
        DealerMapper.class,
        TransactionAccountMapper.class,
        PlaceholderMapper.class,
        UniversallyUniqueMappingMapper.class,
        PrepaymentMappingMapper.class,
        BusinessDocumentMapper.class,
    }
)
public interface PrepaymentAccountMapper extends EntityMapper<PrepaymentAccountDTO, PrepaymentAccount> {
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "prepaymentTransaction", source = "prepaymentTransaction", qualifiedByName = "paymentNumber")
    @Mapping(target = "serviceOutlet", source = "serviceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    @Mapping(target = "debitAccount", source = "debitAccount", qualifiedByName = "accountName")
    @Mapping(target = "transferAccount", source = "transferAccount", qualifiedByName = "accountName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "generalParameters", source = "generalParameters", qualifiedByName = "mappedValueSet")
    @Mapping(target = "prepaymentParameters", source = "prepaymentParameters", qualifiedByName = "parameterKeySet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    PrepaymentAccountDTO toDto(PrepaymentAccount s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeGeneralParameters", ignore = true)
    @Mapping(target = "removePrepaymentParameters", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    PrepaymentAccount toEntity(PrepaymentAccountDTO prepaymentAccountDTO);

    @Named("catalogueNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "catalogueNumber", source = "catalogueNumber")
    PrepaymentAccountDTO toDtoCatalogueNumber(PrepaymentAccount prepaymentAccount);
}
