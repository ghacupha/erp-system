package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.FixedAssetDepreciationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FixedAssetDepreciation} and its DTO {@link FixedAssetDepreciationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FixedAssetDepreciationMapper extends EntityMapper<FixedAssetDepreciationDTO, FixedAssetDepreciation> {}
