package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.FixedAssetDepreciationDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FixedAssetDepreciation} and its DTO {@link FixedAssetDepreciationDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface FixedAssetDepreciationMapper extends EntityMapper<FixedAssetDepreciationDTO, FixedAssetDepreciation> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    FixedAssetDepreciationDTO toDto(FixedAssetDepreciation s);

    @Mapping(target = "removePlaceholder", ignore = true)
    FixedAssetDepreciation toEntity(FixedAssetDepreciationDTO fixedAssetDepreciationDTO);
}
