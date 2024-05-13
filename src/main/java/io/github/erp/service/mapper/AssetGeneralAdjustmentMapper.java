package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.AssetGeneralAdjustment;
import io.github.erp.service.dto.AssetGeneralAdjustmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetGeneralAdjustment} and its DTO {@link AssetGeneralAdjustmentDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DepreciationPeriodMapper.class, AssetRegistrationMapper.class, ApplicationUserMapper.class, PlaceholderMapper.class }
)
public interface AssetGeneralAdjustmentMapper extends EntityMapper<AssetGeneralAdjustmentDTO, AssetGeneralAdjustment> {
    @Mapping(target = "effectivePeriod", source = "effectivePeriod", qualifiedByName = "periodCode")
    @Mapping(target = "assetRegistration", source = "assetRegistration", qualifiedByName = "assetNumber")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "lastAccessedBy", source = "lastAccessedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "placeholder", source = "placeholder", qualifiedByName = "description")
    AssetGeneralAdjustmentDTO toDto(AssetGeneralAdjustment s);
}
