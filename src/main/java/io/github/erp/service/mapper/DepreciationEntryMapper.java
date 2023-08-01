package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IV No 2 (Ehud Series) Server ver 1.3.2
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
