package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.IsoCountryCode;
import io.github.erp.service.dto.IsoCountryCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IsoCountryCode} and its DTO {@link IsoCountryCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IsoCountryCodeMapper extends EntityMapper<IsoCountryCodeDTO, IsoCountryCode> {
    @Named("countryDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "countryDescription", source = "countryDescription")
    IsoCountryCodeDTO toDtoCountryDescription(IsoCountryCode isoCountryCode);
}
