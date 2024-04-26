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
import io.github.erp.domain.GdiTransactionDataIndex;
import io.github.erp.service.dto.GdiTransactionDataIndexDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GdiTransactionDataIndex} and its DTO {@link GdiTransactionDataIndexDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { GdiMasterDataIndexMapper.class, BusinessTeamMapper.class, BusinessDocumentMapper.class, PlaceholderMapper.class }
)
public interface GdiTransactionDataIndexMapper extends EntityMapper<GdiTransactionDataIndexDTO, GdiTransactionDataIndex> {
    @Mapping(target = "masterDataItems", source = "masterDataItems", qualifiedByName = "entityNameSet")
    @Mapping(target = "businessTeam", source = "businessTeam", qualifiedByName = "businessTeam")
    @Mapping(target = "dataSetTemplate", source = "dataSetTemplate", qualifiedByName = "documentTitle")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    GdiTransactionDataIndexDTO toDto(GdiTransactionDataIndex s);

    @Mapping(target = "removeMasterDataItem", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    GdiTransactionDataIndex toEntity(GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO);
}
