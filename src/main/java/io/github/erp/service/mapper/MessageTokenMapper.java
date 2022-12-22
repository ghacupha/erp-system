package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.2.0
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
import io.github.erp.domain.MessageToken;
import io.github.erp.service.dto.MessageTokenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MessageToken} and its DTO {@link MessageTokenDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface MessageTokenMapper extends EntityMapper<MessageTokenDTO, MessageToken> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    MessageTokenDTO toDto(MessageToken s);

    @Mapping(target = "removePlaceholder", ignore = true)
    MessageToken toEntity(MessageTokenDTO messageTokenDTO);
}
