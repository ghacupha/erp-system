package io.github.erp.internal.service;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
