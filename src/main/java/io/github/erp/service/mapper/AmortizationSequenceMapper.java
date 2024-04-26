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
import io.github.erp.domain.AmortizationSequence;
import io.github.erp.service.dto.AmortizationSequenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AmortizationSequence} and its DTO {@link AmortizationSequenceDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PrepaymentAccountMapper.class,
        AmortizationRecurrenceMapper.class,
        PlaceholderMapper.class,
        PrepaymentMappingMapper.class,
        UniversallyUniqueMappingMapper.class,
    }
)
public interface AmortizationSequenceMapper extends EntityMapper<AmortizationSequenceDTO, AmortizationSequence> {
    @Mapping(target = "prepaymentAccount", source = "prepaymentAccount", qualifiedByName = "catalogueNumber")
    @Mapping(target = "amortizationRecurrence", source = "amortizationRecurrence", qualifiedByName = "particulars")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "prepaymentMappings", source = "prepaymentMappings", qualifiedByName = "parameterSet")
    @Mapping(target = "applicationParameters", source = "applicationParameters", qualifiedByName = "mappedValueSet")
    AmortizationSequenceDTO toDto(AmortizationSequence s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePrepaymentMapping", ignore = true)
    @Mapping(target = "removeApplicationParameters", ignore = true)
    AmortizationSequence toEntity(AmortizationSequenceDTO amortizationSequenceDTO);
}
