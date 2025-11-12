package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.RouModelMetadata;
import io.github.erp.service.dto.RouModelMetadataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RouModelMetadata} and its DTO {@link RouModelMetadataDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { IFRS16LeaseContractMapper.class, TransactionAccountMapper.class, AssetCategoryMapper.class, BusinessDocumentMapper.class }
)
public interface RouModelMetadataMapper extends EntityMapper<RouModelMetadataDTO, RouModelMetadata> {
    @Mapping(target = "ifrs16LeaseContract", source = "ifrs16LeaseContract", qualifiedByName = "shortTitle")
    @Mapping(target = "assetAccount", source = "assetAccount", qualifiedByName = "accountName")
    @Mapping(target = "depreciationAccount", source = "depreciationAccount", qualifiedByName = "accountName")
    @Mapping(target = "accruedDepreciationAccount", source = "accruedDepreciationAccount", qualifiedByName = "accountName")
    @Mapping(target = "assetCategory", source = "assetCategory", qualifiedByName = "assetCategoryName")
    @Mapping(target = "documentAttachments", source = "documentAttachments", qualifiedByName = "documentTitleSet")
    RouModelMetadataDTO toDto(RouModelMetadata s);

    @Mapping(target = "removeDocumentAttachments", ignore = true)
    RouModelMetadata toEntity(RouModelMetadataDTO rouModelMetadataDTO);

    @Named("modelTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "modelTitle", source = "modelTitle")
    RouModelMetadataDTO toDtoModelTitle(RouModelMetadata rouModelMetadata);
}
