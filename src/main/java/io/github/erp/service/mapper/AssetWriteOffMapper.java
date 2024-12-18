package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.AssetWriteOff;
import io.github.erp.service.dto.AssetWriteOffDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetWriteOff} and its DTO {@link AssetWriteOffDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { ApplicationUserMapper.class, DepreciationPeriodMapper.class, PlaceholderMapper.class, AssetRegistrationMapper.class }
)
public interface AssetWriteOffMapper extends EntityMapper<AssetWriteOffDTO, AssetWriteOff> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "modifiedBy", source = "modifiedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "lastAccessedBy", source = "lastAccessedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "effectivePeriod", source = "effectivePeriod", qualifiedByName = "startDate")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "assetWrittenOff", source = "assetWrittenOff", qualifiedByName = "assetNumber")
    AssetWriteOffDTO toDto(AssetWriteOff s);

    @Mapping(target = "removePlaceholder", ignore = true)
    AssetWriteOff toEntity(AssetWriteOffDTO assetWriteOffDTO);
}
