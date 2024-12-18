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

import io.github.erp.domain.RouInitialDirectCost;
import io.github.erp.service.dto.RouInitialDirectCostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RouInitialDirectCost} and its DTO {@link RouInitialDirectCostDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { IFRS16LeaseContractMapper.class, SettlementMapper.class, TransactionAccountMapper.class, PlaceholderMapper.class }
)
public interface RouInitialDirectCostMapper extends EntityMapper<RouInitialDirectCostDTO, RouInitialDirectCost> {
    @Mapping(target = "leaseContract", source = "leaseContract", qualifiedByName = "shortTitle")
    @Mapping(target = "settlementDetails", source = "settlementDetails", qualifiedByName = "paymentNumber")
    @Mapping(target = "targetROUAccount", source = "targetROUAccount", qualifiedByName = "accountName")
    @Mapping(target = "transferAccount", source = "transferAccount", qualifiedByName = "accountName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    RouInitialDirectCostDTO toDto(RouInitialDirectCost s);

    @Mapping(target = "removePlaceholder", ignore = true)
    RouInitialDirectCost toEntity(RouInitialDirectCostDTO rouInitialDirectCostDTO);
}
