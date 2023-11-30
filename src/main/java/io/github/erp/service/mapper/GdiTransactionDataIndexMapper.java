package io.github.erp.service.mapper;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
