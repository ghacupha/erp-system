package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

import io.github.erp.domain.RouAccountBalanceReport;
import io.github.erp.service.dto.RouAccountBalanceReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RouAccountBalanceReport} and its DTO {@link RouAccountBalanceReportDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicationUserMapper.class, FiscalMonthMapper.class })
public interface RouAccountBalanceReportMapper extends EntityMapper<RouAccountBalanceReportDTO, RouAccountBalanceReport> {
    @Mapping(target = "requestedBy", source = "requestedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "reportingMonth", source = "reportingMonth", qualifiedByName = "fiscalMonthCode")
    RouAccountBalanceReportDTO toDto(RouAccountBalanceReport s);
}
