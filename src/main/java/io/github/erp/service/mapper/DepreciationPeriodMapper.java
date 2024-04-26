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
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepreciationPeriod} and its DTO {@link DepreciationPeriodDTO}.
 */
@Mapper(componentModel = "spring", uses = { FiscalMonthMapper.class })
public interface DepreciationPeriodMapper extends EntityMapper<DepreciationPeriodDTO, DepreciationPeriod> {
    @Mapping(target = "previousPeriod", source = "previousPeriod", qualifiedByName = "endDate")
    @Mapping(target = "fiscalMonth", source = "fiscalMonth", qualifiedByName = "fiscalMonthCode")
    DepreciationPeriodDTO toDto(DepreciationPeriod s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepreciationPeriodDTO toDtoId(DepreciationPeriod depreciationPeriod);

    @Named("periodCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "periodCode", source = "periodCode")
    DepreciationPeriodDTO toDtoPeriodCode(DepreciationPeriod depreciationPeriod);

    @Named("endDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "endDate", source = "endDate")
    DepreciationPeriodDTO toDtoEndDate(DepreciationPeriod depreciationPeriod);

    @Named("startDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "startDate", source = "startDate")
    DepreciationPeriodDTO toDtoStartDate(DepreciationPeriod depreciationPeriod);
}
