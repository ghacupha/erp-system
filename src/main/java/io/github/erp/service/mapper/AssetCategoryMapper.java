package io.github.erp.service.mapper;

import io.github.erp.domain.AssetCategory;
import io.github.erp.service.dto.AssetCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetCategory} and its DTO {@link AssetCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepreciationMethodMapper.class, PlaceholderMapper.class })
public interface AssetCategoryMapper extends EntityMapper<AssetCategoryDTO, AssetCategory> {
    @Mapping(target = "depreciationMethod", source = "depreciationMethod", qualifiedByName = "depreciationMethodName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    AssetCategoryDTO toDto(AssetCategory s);

    @Mapping(target = "removePlaceholder", ignore = true)
    AssetCategory toEntity(AssetCategoryDTO assetCategoryDTO);

    @Named("assetCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "assetCategoryName", source = "assetCategoryName")
    AssetCategoryDTO toDtoAssetCategoryName(AssetCategory assetCategory);
}
