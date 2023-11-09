package io.github.erp.service.mapper;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

import io.github.erp.domain.AssetWarranty;
import io.github.erp.service.dto.AssetWarrantyDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetWarranty} and its DTO {@link AssetWarrantyDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class, DealerMapper.class, BusinessDocumentMapper.class }
)
public interface AssetWarrantyMapper extends EntityMapper<AssetWarrantyDTO, AssetWarranty> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "dealerName")
    @Mapping(target = "warrantyAttachments", source = "warrantyAttachments", qualifiedByName = "documentTitleSet")
    AssetWarrantyDTO toDto(AssetWarranty s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    @Mapping(target = "removeWarrantyAttachment", ignore = true)
    AssetWarranty toEntity(AssetWarrantyDTO assetWarrantyDTO);

    @Named("descriptionSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    Set<AssetWarrantyDTO> toDtoDescriptionSet(Set<AssetWarranty> assetWarranty);
}
