package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.5
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
