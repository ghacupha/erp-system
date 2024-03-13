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

import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IFRS16LeaseContract} and its DTO {@link IFRS16LeaseContractDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { ServiceOutletMapper.class, DealerMapper.class, FiscalMonthMapper.class, BusinessDocumentMapper.class }
)
public interface IFRS16LeaseContractMapper extends EntityMapper<IFRS16LeaseContractDTO, IFRS16LeaseContract> {
    @Mapping(target = "superintendentServiceOutlet", source = "superintendentServiceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "mainDealer", source = "mainDealer", qualifiedByName = "dealerName")
    @Mapping(target = "firstReportingPeriod", source = "firstReportingPeriod", qualifiedByName = "fiscalMonthCode")
    @Mapping(target = "lastReportingPeriod", source = "lastReportingPeriod", qualifiedByName = "fiscalMonthCode")
    @Mapping(target = "leaseContractDocument", source = "leaseContractDocument", qualifiedByName = "documentTitle")
    @Mapping(target = "leaseContractCalculations", source = "leaseContractCalculations", qualifiedByName = "documentTitle")
    IFRS16LeaseContractDTO toDto(IFRS16LeaseContract s);

    @Named("shortTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "shortTitle", source = "shortTitle")
    IFRS16LeaseContractDTO toDtoShortTitle(IFRS16LeaseContract iFRS16LeaseContract);

    @Named("bookingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingId", source = "bookingId")
    IFRS16LeaseContractDTO toDtoBookingId(IFRS16LeaseContract iFRS16LeaseContract);
}
