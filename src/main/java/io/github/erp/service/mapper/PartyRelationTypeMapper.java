package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import io.github.erp.domain.PartyRelationType;
import io.github.erp.service.dto.PartyRelationTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PartyRelationType} and its DTO {@link PartyRelationTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PartyRelationTypeMapper extends EntityMapper<PartyRelationTypeDTO, PartyRelationType> {
    @Named("partyRelationType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "partyRelationType", source = "partyRelationType")
    PartyRelationTypeDTO toDtoPartyRelationType(PartyRelationType partyRelationType);
}
