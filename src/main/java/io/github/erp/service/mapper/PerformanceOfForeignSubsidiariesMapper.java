package io.github.erp.service.mapper;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
import io.github.erp.domain.PerformanceOfForeignSubsidiaries;
import io.github.erp.service.dto.PerformanceOfForeignSubsidiariesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PerformanceOfForeignSubsidiaries} and its DTO {@link PerformanceOfForeignSubsidiariesDTO}.
 */
@Mapper(componentModel = "spring", uses = { InstitutionCodeMapper.class, IsoCountryCodeMapper.class })
public interface PerformanceOfForeignSubsidiariesMapper
    extends EntityMapper<PerformanceOfForeignSubsidiariesDTO, PerformanceOfForeignSubsidiaries> {
    @Mapping(target = "bankCode", source = "bankCode", qualifiedByName = "institutionName")
    @Mapping(target = "subsidiaryCountryCode", source = "subsidiaryCountryCode", qualifiedByName = "countryDescription")
    PerformanceOfForeignSubsidiariesDTO toDto(PerformanceOfForeignSubsidiaries s);
}
