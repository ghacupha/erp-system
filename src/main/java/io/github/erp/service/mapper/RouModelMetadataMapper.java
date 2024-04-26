package io.github.erp.service.mapper;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
