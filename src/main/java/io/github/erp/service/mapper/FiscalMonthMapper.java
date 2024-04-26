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
import io.github.erp.domain.FiscalMonth;
import io.github.erp.service.dto.FiscalMonthDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FiscalMonth} and its DTO {@link FiscalMonthDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { FiscalYearMapper.class, PlaceholderMapper.class, UniversallyUniqueMappingMapper.class, FiscalQuarterMapper.class }
)
public interface FiscalMonthMapper extends EntityMapper<FiscalMonthDTO, FiscalMonth> {
    @Mapping(target = "fiscalYear", source = "fiscalYear", qualifiedByName = "fiscalYearCode")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "fiscalQuarter", source = "fiscalQuarter", qualifiedByName = "fiscalQuarterCode")
    FiscalMonthDTO toDto(FiscalMonth s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    FiscalMonth toEntity(FiscalMonthDTO fiscalMonthDTO);

    @Named("startDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "startDate", source = "startDate")
    FiscalMonthDTO toDtoStartDate(FiscalMonth fiscalMonth);

    @Named("endDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "endDate", source = "endDate")
    FiscalMonthDTO toDtoEndDate(FiscalMonth fiscalMonth);

    @Named("fiscalMonthCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fiscalMonthCode", source = "fiscalMonthCode")
    FiscalMonthDTO toDtoFiscalMonthCode(FiscalMonth fiscalMonth);
}
