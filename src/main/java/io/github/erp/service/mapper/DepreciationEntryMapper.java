package io.github.erp.service.mapper;

import io.github.erp.domain.DepreciationEntry;
import io.github.erp.service.dto.DepreciationEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepreciationEntry} and its DTO {@link DepreciationEntryDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ServiceOutletMapper.class,
        AssetCategoryMapper.class,
        DepreciationMethodMapper.class,
        AssetRegistrationMapper.class,
        DepreciationPeriodMapper.class,
    }
)
public interface DepreciationEntryMapper extends EntityMapper<DepreciationEntryDTO, DepreciationEntry> {
    @Mapping(target = "serviceOutlet", source = "serviceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "assetCategory", source = "assetCategory", qualifiedByName = "assetCategoryName")
    @Mapping(target = "depreciationMethod", source = "depreciationMethod", qualifiedByName = "depreciationMethodName")
    @Mapping(target = "assetRegistration", source = "assetRegistration", qualifiedByName = "assetNumber")
    @Mapping(target = "depreciationPeriod", source = "depreciationPeriod", qualifiedByName = "endDate")
    DepreciationEntryDTO toDto(DepreciationEntry s);
}
