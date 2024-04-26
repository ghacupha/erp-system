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
