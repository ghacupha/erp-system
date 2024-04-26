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
import io.github.erp.domain.FiscalQuarter;
import io.github.erp.service.dto.FiscalQuarterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FiscalQuarter} and its DTO {@link FiscalQuarterDTO}.
 */
@Mapper(componentModel = "spring", uses = { FiscalYearMapper.class, PlaceholderMapper.class, UniversallyUniqueMappingMapper.class })
public interface FiscalQuarterMapper extends EntityMapper<FiscalQuarterDTO, FiscalQuarter> {
    @Mapping(target = "fiscalYear", source = "fiscalYear", qualifiedByName = "fiscalYearCode")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    FiscalQuarterDTO toDto(FiscalQuarter s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    FiscalQuarter toEntity(FiscalQuarterDTO fiscalQuarterDTO);

    @Named("fiscalQuarterCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fiscalQuarterCode", source = "fiscalQuarterCode")
    FiscalQuarterDTO toDtoFiscalQuarterCode(FiscalQuarter fiscalQuarter);
}
