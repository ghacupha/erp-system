package io.github.erp.service.mapper;

import io.github.erp.domain.FixedAssetNetBookValue;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FixedAssetNetBookValue} and its DTO {@link FixedAssetNetBookValueDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface FixedAssetNetBookValueMapper extends EntityMapper<FixedAssetNetBookValueDTO, FixedAssetNetBookValue> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    FixedAssetNetBookValueDTO toDto(FixedAssetNetBookValue s);

    @Mapping(target = "removePlaceholder", ignore = true)
    FixedAssetNetBookValue toEntity(FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO);
}
