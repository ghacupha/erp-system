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

import io.github.erp.domain.LeasePeriod;
import io.github.erp.service.dto.LeasePeriodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeasePeriod} and its DTO {@link LeasePeriodDTO}.
 */
@Mapper(componentModel = "spring", uses = { FiscalMonthMapper.class })
public interface LeasePeriodMapper extends EntityMapper<LeasePeriodDTO, LeasePeriod> {
    @Mapping(target = "fiscalMonth", source = "fiscalMonth", qualifiedByName = "fiscalMonthCode")
    LeasePeriodDTO toDto(LeasePeriod s);

    @Named("periodCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "periodCode", source = "periodCode")
    LeasePeriodDTO toDtoPeriodCode(LeasePeriod leasePeriod);
}
