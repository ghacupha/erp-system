package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.TransactionAccount;
import io.github.erp.service.dto.TransactionAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransactionAccount} and its DTO {@link TransactionAccountDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        TransactionAccountLedgerMapper.class,
        TransactionAccountCategoryMapper.class,
        PlaceholderMapper.class,
        ServiceOutletMapper.class,
        SettlementCurrencyMapper.class,
        ReportingEntityMapper.class,
    }
)
public interface TransactionAccountMapper extends EntityMapper<TransactionAccountDTO, TransactionAccount> {
    @Mapping(target = "accountLedger", source = "accountLedger", qualifiedByName = "ledgerName")
    @Mapping(target = "accountCategory", source = "accountCategory", qualifiedByName = "name")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "serviceOutlet", source = "serviceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "institution", source = "institution", qualifiedByName = "entityName")
    TransactionAccountDTO toDto(TransactionAccount s);

    @Mapping(target = "removePlaceholder", ignore = true)
    TransactionAccount toEntity(TransactionAccountDTO transactionAccountDTO);

    @Named("accountName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "accountName", source = "accountName")
    TransactionAccountDTO toDtoAccountName(TransactionAccount transactionAccount);

    @Named("accountNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "accountNumber", source = "accountNumber")
    TransactionAccountDTO toDtoAccountNumber(TransactionAccount transactionAccount);
}
