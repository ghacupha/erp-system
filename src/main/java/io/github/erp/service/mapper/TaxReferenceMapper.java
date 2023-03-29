package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.2-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.TaxReference;
import io.github.erp.service.dto.TaxReferenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxReference} and its DTO {@link TaxReferenceDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface TaxReferenceMapper extends EntityMapper<TaxReferenceDTO, TaxReference> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    TaxReferenceDTO toDto(TaxReference s);

    @Mapping(target = "removePlaceholder", ignore = true)
    TaxReference toEntity(TaxReferenceDTO taxReferenceDTO);
}
