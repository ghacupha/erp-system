package io.github.erp.service.mapper;

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
