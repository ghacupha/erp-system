package io.github.erp.service.mapper;

import io.github.erp.domain.FixedAssetAcquisition;
import io.github.erp.service.dto.FixedAssetAcquisitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FixedAssetAcquisition} and its DTO {@link FixedAssetAcquisitionDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface FixedAssetAcquisitionMapper extends EntityMapper<FixedAssetAcquisitionDTO, FixedAssetAcquisition> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    FixedAssetAcquisitionDTO toDto(FixedAssetAcquisition s);

    @Mapping(target = "removePlaceholder", ignore = true)
    FixedAssetAcquisition toEntity(FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO);
}
