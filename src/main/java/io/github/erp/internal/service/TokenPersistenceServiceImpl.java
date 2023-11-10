package io.github.erp.internal.service;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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
import io.github.erp.domain.MessageToken;
import io.github.erp.internal.framework.service.TokenPersistenceService;
import io.github.erp.service.MessageTokenService;
import io.github.erp.service.dto.MessageTokenDTO;
import io.github.erp.service.mapper.MessageTokenMapper;
import org.springframework.stereotype.Service;

@Service("tokenPersistenceServiceImpl")
public class TokenPersistenceServiceImpl implements TokenPersistenceService<MessageTokenDTO, MessageToken>{

    private final MessageTokenService MessageTokenService;
    private final MessageTokenMapper messageTokenMapper;

    public TokenPersistenceServiceImpl(MessageTokenService MessageTokenService, MessageTokenMapper messageTokenMapper) {
        this.MessageTokenService = MessageTokenService;
        this.messageTokenMapper = messageTokenMapper;
    }

    @Override
    public MessageTokenDTO save(MessageToken persistentToken) {
        return MessageTokenService.save(messageTokenMapper.toDto(persistentToken));
    }
}
