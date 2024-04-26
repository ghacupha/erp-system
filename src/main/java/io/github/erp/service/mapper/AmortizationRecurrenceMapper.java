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
import io.github.erp.domain.AmortizationRecurrence;
import io.github.erp.service.dto.AmortizationRecurrenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AmortizationRecurrence} and its DTO {@link AmortizationRecurrenceDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        PrepaymentMappingMapper.class,
        UniversallyUniqueMappingMapper.class,
        DepreciationMethodMapper.class,
        PrepaymentAccountMapper.class,
    }
)
public interface AmortizationRecurrenceMapper extends EntityMapper<AmortizationRecurrenceDTO, AmortizationRecurrence> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "parameterSet")
    @Mapping(target = "applicationParameters", source = "applicationParameters", qualifiedByName = "mappedValueSet")
    @Mapping(target = "depreciationMethod", source = "depreciationMethod", qualifiedByName = "description")
    @Mapping(target = "prepaymentAccount", source = "prepaymentAccount", qualifiedByName = "catalogueNumber")
    AmortizationRecurrenceDTO toDto(AmortizationRecurrence s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeParameters", ignore = true)
    @Mapping(target = "removeApplicationParameters", ignore = true)
    AmortizationRecurrence toEntity(AmortizationRecurrenceDTO amortizationRecurrenceDTO);

    @Named("particulars")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "particulars", source = "particulars")
    AmortizationRecurrenceDTO toDtoParticulars(AmortizationRecurrence amortizationRecurrence);
}
