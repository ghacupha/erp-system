package io.github.erp.service.mapper;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
