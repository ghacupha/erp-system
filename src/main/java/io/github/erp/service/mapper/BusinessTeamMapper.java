package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.BusinessTeam;
import io.github.erp.service.dto.BusinessTeamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessTeam} and its DTO {@link BusinessTeamDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface BusinessTeamMapper extends EntityMapper<BusinessTeamDTO, BusinessTeam> {
    @Mapping(target = "teamMembers", source = "teamMembers", qualifiedByName = "login")
    BusinessTeamDTO toDto(BusinessTeam s);

    @Named("businessTeam")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "businessTeam", source = "businessTeam")
    BusinessTeamDTO toDtoBusinessTeam(BusinessTeam businessTeam);
}
