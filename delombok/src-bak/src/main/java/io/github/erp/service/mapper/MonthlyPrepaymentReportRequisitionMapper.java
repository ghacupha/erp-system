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
import io.github.erp.domain.MonthlyPrepaymentReportRequisition;
import io.github.erp.service.dto.MonthlyPrepaymentReportRequisitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonthlyPrepaymentReportRequisition} and its DTO {@link MonthlyPrepaymentReportRequisitionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicationUserMapper.class, FiscalYearMapper.class })
public interface MonthlyPrepaymentReportRequisitionMapper
    extends EntityMapper<MonthlyPrepaymentReportRequisitionDTO, MonthlyPrepaymentReportRequisition> {
    @Mapping(target = "requestedBy", source = "requestedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "lastAccessedBy", source = "lastAccessedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "fiscalYear", source = "fiscalYear", qualifiedByName = "fiscalYearCode")
    MonthlyPrepaymentReportRequisitionDTO toDto(MonthlyPrepaymentReportRequisition s);
}