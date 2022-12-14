package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.8-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
