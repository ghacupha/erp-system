package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.NbvCompilationJob;
import io.github.erp.service.dto.NbvCompilationJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NbvCompilationJob} and its DTO {@link NbvCompilationJobDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepreciationPeriodMapper.class, ApplicationUserMapper.class })
public interface NbvCompilationJobMapper extends EntityMapper<NbvCompilationJobDTO, NbvCompilationJob> {
    @Mapping(target = "activePeriod", source = "activePeriod", qualifiedByName = "endDate")
    @Mapping(target = "initiatedBy", source = "initiatedBy", qualifiedByName = "applicationIdentity")
    NbvCompilationJobDTO toDto(NbvCompilationJob s);

    @Named("jobTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "jobTitle", source = "jobTitle")
    NbvCompilationJobDTO toDtoJobTitle(NbvCompilationJob nbvCompilationJob);
}
