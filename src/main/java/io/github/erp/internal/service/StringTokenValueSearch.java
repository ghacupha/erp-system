package io.github.erp.internal.service;

/*-
 * Erp System - Mark II No 11 (Artaxerxes Series)
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
import tech.jhipster.service.filter.StringFilter;
import io.github.erp.internal.framework.fileProcessing.TokenValueSearch;
import io.github.erp.service.MessageTokenQueryService;
import io.github.erp.service.criteria.MessageTokenCriteria;
import io.github.erp.service.dto.MessageTokenDTO;
import org.springframework.stereotype.Service;

/**
 * Implementation of token-search where the token value itself is of the value string
 */
@Service("stringTokenValueSearch")
public class StringTokenValueSearch implements TokenValueSearch<String> {

    private final MessageTokenQueryService messageTokenQueryService;

    public StringTokenValueSearch(final MessageTokenQueryService messageTokenQueryService) {
        this.messageTokenQueryService = messageTokenQueryService;
    }

    public MessageTokenDTO getMessageToken(final String tokenValue) {
        StringFilter tokenFilter = new StringFilter();
        tokenFilter.setEquals(tokenValue);
        MessageTokenCriteria tokenValueCriteria = new MessageTokenCriteria();
        tokenValueCriteria.setTokenValue(tokenFilter);
        return messageTokenQueryService.findByCriteria(tokenValueCriteria).get(0);
    }
}
