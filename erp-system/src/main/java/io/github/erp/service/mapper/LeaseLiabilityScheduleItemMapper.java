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

import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaseLiabilityScheduleItem} and its DTO {@link LeaseLiabilityScheduleItemDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        UniversallyUniqueMappingMapper.class,
        LeaseAmortizationScheduleMapper.class,
        IFRS16LeaseContractMapper.class,
        LeaseLiabilityMapper.class,
        LeaseRepaymentPeriodMapper.class,
    }
)
public interface LeaseLiabilityScheduleItemMapper extends EntityMapper<LeaseLiabilityScheduleItemDTO, LeaseLiabilityScheduleItem> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "leaseAmortizationSchedule", source = "leaseAmortizationSchedule", qualifiedByName = "id")
    @Mapping(target = "leaseContract", source = "leaseContract", qualifiedByName = "bookingId")
    @Mapping(target = "leaseLiability", source = "leaseLiability", qualifiedByName = "id")
    @Mapping(target = "leasePeriod", source = "leasePeriod", qualifiedByName = "periodCode")
    LeaseLiabilityScheduleItemDTO toDto(LeaseLiabilityScheduleItem s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    LeaseLiabilityScheduleItem toEntity(LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO);
}
