package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaseLiabilityScheduleItem} and its DTO {@link LeaseLiabilityScheduleItemDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PlaceholderMapper.class, LeaseContractMapper.class, LeaseModelMetadataMapper.class, UniversallyUniqueMappingMapper.class }
)
public interface LeaseLiabilityScheduleItemMapper extends EntityMapper<LeaseLiabilityScheduleItemDTO, LeaseLiabilityScheduleItem> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "leaseContract", source = "leaseContract", qualifiedByName = "bookingId")
    @Mapping(target = "leaseModelMetadata", source = "leaseModelMetadata", qualifiedByName = "modelTitle")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    LeaseLiabilityScheduleItemDTO toDto(LeaseLiabilityScheduleItem s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    LeaseLiabilityScheduleItem toEntity(LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO);
}
