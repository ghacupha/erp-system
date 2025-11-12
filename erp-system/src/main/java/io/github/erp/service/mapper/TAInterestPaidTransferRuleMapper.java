package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.TAInterestPaidTransferRule;
import io.github.erp.service.dto.TAInterestPaidTransferRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TAInterestPaidTransferRule} and its DTO {@link TAInterestPaidTransferRuleDTO}.
 */
@Mapper(componentModel = "spring", uses = { IFRS16LeaseContractMapper.class, TransactionAccountMapper.class, PlaceholderMapper.class })
public interface TAInterestPaidTransferRuleMapper extends EntityMapper<TAInterestPaidTransferRuleDTO, TAInterestPaidTransferRule> {
    @Mapping(target = "leaseContract", source = "leaseContract", qualifiedByName = "shortTitle")
    @Mapping(target = "debit", source = "debit", qualifiedByName = "accountName")
    @Mapping(target = "credit", source = "credit", qualifiedByName = "accountName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    TAInterestPaidTransferRuleDTO toDto(TAInterestPaidTransferRule s);

    @Mapping(target = "removePlaceholder", ignore = true)
    TAInterestPaidTransferRule toEntity(TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO);
}
