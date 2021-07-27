package io.github.erp.service.mapper;


import io.github.erp.domain.*;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FixedAssetNetBookValue} and its DTO {@link FixedAssetNetBookValueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FixedAssetNetBookValueMapper extends EntityMapper<FixedAssetNetBookValueDTO, FixedAssetNetBookValue> {



    default FixedAssetNetBookValue fromId(Long id) {
        if (id == null) {
            return null;
        }
        FixedAssetNetBookValue fixedAssetNetBookValue = new FixedAssetNetBookValue();
        fixedAssetNetBookValue.setId(id);
        return fixedAssetNetBookValue;
    }
}
