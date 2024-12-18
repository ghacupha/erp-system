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

import io.github.erp.domain.RouDepreciationEntry;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RouDepreciationEntry} and its DTO {@link RouDepreciationEntryDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        TransactionAccountMapper.class,
        AssetCategoryMapper.class,
        IFRS16LeaseContractMapper.class,
        RouModelMetadataMapper.class,
        LeasePeriodMapper.class,
    }
)
public interface RouDepreciationEntryMapper extends EntityMapper<RouDepreciationEntryDTO, RouDepreciationEntry> {
    @Mapping(target = "debitAccount", source = "debitAccount", qualifiedByName = "accountName")
    @Mapping(target = "creditAccount", source = "creditAccount", qualifiedByName = "accountName")
    @Mapping(target = "assetCategory", source = "assetCategory", qualifiedByName = "assetCategoryName")
    @Mapping(target = "leaseContract", source = "leaseContract", qualifiedByName = "bookingId")
    @Mapping(target = "rouMetadata", source = "rouMetadata", qualifiedByName = "modelTitle")
    @Mapping(target = "leasePeriod", source = "leasePeriod", qualifiedByName = "periodCode")
    RouDepreciationEntryDTO toDto(RouDepreciationEntry s);
}
