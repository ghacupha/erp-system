package io.github.erp.internal.service;

/*-
 * Leassets Server - Leases and assets management platform
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.LeassetsMessageToken;
import io.github.erp.internal.framework.service.TokenPersistenceService;
import io.github.erp.service.LeassetsMessageTokenService;
import io.github.erp.service.dto.LeassetsMessageTokenDTO;
import io.github.erp.service.mapper.LeassetsMessageTokenMapper;
import org.springframework.stereotype.Service;

@Service("tokenPersistenceServiceImpl")
public class TokenPersistenceServiceImpl implements TokenPersistenceService<LeassetsMessageTokenDTO, LeassetsMessageToken>{

    private final LeassetsMessageTokenService leassetsMessageTokenService;
    private final LeassetsMessageTokenMapper messageTokenMapper;

    public TokenPersistenceServiceImpl(LeassetsMessageTokenService leassetsMessageTokenService, LeassetsMessageTokenMapper messageTokenMapper) {
        this.leassetsMessageTokenService = leassetsMessageTokenService;
        this.messageTokenMapper = messageTokenMapper;
    }

    @Override
    public LeassetsMessageTokenDTO save(LeassetsMessageToken persistentToken) {
        return leassetsMessageTokenService.save(messageTokenMapper.toDto(persistentToken));
    }
}
