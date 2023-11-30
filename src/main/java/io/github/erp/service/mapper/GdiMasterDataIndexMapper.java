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

import io.github.erp.domain.GdiMasterDataIndex;
import io.github.erp.service.dto.GdiMasterDataIndexDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GdiMasterDataIndex} and its DTO {@link GdiMasterDataIndexDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GdiMasterDataIndexMapper extends EntityMapper<GdiMasterDataIndexDTO, GdiMasterDataIndex> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GdiMasterDataIndexDTO toDtoId(GdiMasterDataIndex gdiMasterDataIndex);

    @Named("entityNameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "entityName", source = "entityName")
    Set<GdiMasterDataIndexDTO> toDtoEntityNameSet(Set<GdiMasterDataIndex> gdiMasterDataIndex);
}
