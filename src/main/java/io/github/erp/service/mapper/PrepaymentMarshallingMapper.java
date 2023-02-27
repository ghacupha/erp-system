package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.5.0
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
import io.github.erp.domain.PrepaymentMarshalling;
import io.github.erp.service.dto.PrepaymentMarshallingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrepaymentMarshalling} and its DTO {@link PrepaymentMarshallingDTO}.
 */
@Mapper(componentModel = "spring", uses = { PrepaymentAccountMapper.class, PlaceholderMapper.class })
public interface PrepaymentMarshallingMapper extends EntityMapper<PrepaymentMarshallingDTO, PrepaymentMarshalling> {
    @Mapping(target = "prepaymentAccount", source = "prepaymentAccount", qualifiedByName = "catalogueNumber")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    PrepaymentMarshallingDTO toDto(PrepaymentMarshalling s);

    @Mapping(target = "removePlaceholder", ignore = true)
    PrepaymentMarshalling toEntity(PrepaymentMarshallingDTO prepaymentMarshallingDTO);
}
