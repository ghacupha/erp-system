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
