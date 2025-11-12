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
import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NetBookValueEntry} and its DTO {@link NetBookValueEntryDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ServiceOutletMapper.class,
        DepreciationPeriodMapper.class,
        FiscalMonthMapper.class,
        DepreciationMethodMapper.class,
        AssetRegistrationMapper.class,
        AssetCategoryMapper.class,
        PlaceholderMapper.class,
    }
)
public interface NetBookValueEntryMapper extends EntityMapper<NetBookValueEntryDTO, NetBookValueEntry> {
    @Mapping(target = "serviceOutlet", source = "serviceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "depreciationPeriod", source = "depreciationPeriod", qualifiedByName = "endDate")
    @Mapping(target = "fiscalMonth", source = "fiscalMonth", qualifiedByName = "fiscalMonthCode")
    @Mapping(target = "depreciationMethod", source = "depreciationMethod", qualifiedByName = "depreciationMethodName")
    @Mapping(target = "assetRegistration", source = "assetRegistration", qualifiedByName = "id")
    @Mapping(target = "assetCategory", source = "assetCategory", qualifiedByName = "assetCategoryName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    NetBookValueEntryDTO toDto(NetBookValueEntry s);

    @Mapping(target = "removePlaceholder", ignore = true)
    NetBookValueEntry toEntity(NetBookValueEntryDTO netBookValueEntryDTO);
}
