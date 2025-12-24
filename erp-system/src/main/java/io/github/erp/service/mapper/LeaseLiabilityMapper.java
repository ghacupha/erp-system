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
import io.github.erp.domain.LeaseLiability;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaseLiability} and its DTO {@link LeaseLiabilityDTO}.
 */
@Mapper(componentModel = "spring", uses = { LeaseAmortizationCalculationMapper.class, IFRS16LeaseContractMapper.class })
public interface LeaseLiabilityMapper extends EntityMapper<LeaseLiabilityDTO, LeaseLiability> {
    @Mapping(target = "leaseAmortizationCalculation", source = "leaseAmortizationCalculation", qualifiedByName = "id")
    @Mapping(target = "leaseContract", source = "leaseContract", qualifiedByName = "id")
    LeaseLiabilityDTO toDto(LeaseLiability s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LeaseLiabilityDTO toDtoId(LeaseLiability leaseLiability);

    @Named("leaseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "leaseId", source = "leaseId")
    LeaseLiabilityDTO toDtoLeaseId(LeaseLiability leaseLiability);
}
