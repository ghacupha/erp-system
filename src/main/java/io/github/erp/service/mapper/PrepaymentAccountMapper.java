package io.github.erp.service.mapper;

/*-
 * Erp System - Mark II No 4 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
    PrepaymentAccountDTO toDto(PrepaymentAccount s);

    @Mapping(target = "removePlaceholder", ignore = true)
    PrepaymentAccount toEntity(PrepaymentAccountDTO prepaymentAccountDTO);

    @Named("catalogueNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "catalogueNumber", source = "catalogueNumber")
    PrepaymentAccountDTO toDtoCatalogueNumber(PrepaymentAccount prepaymentAccount);
}
