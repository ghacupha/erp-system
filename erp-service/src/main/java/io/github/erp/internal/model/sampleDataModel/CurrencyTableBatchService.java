
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
package io.github.erp.internal.model.sampleDataModel;

import io.github.leassets.internal.framework.BatchService;
import io.github.leassets.repository.CurrencyTableRepository;
import io.github.leassets.repository.search.CurrencyTableSearchRepository;
import io.github.leassets.service.dto.CurrencyTableDTO;
import io.github.leassets.service.mapper.CurrencyTableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("currencyTableBatchService")
public class CurrencyTableBatchService implements BatchService<CurrencyTableDTO> {

    private final CurrencyTableMapper currencyTableMapper;
    private final CurrencyTableRepository currencyTableRepository;
    private final CurrencyTableSearchRepository currencyTableSearchRepository;

    public CurrencyTableBatchService(final CurrencyTableMapper currencyTableMapper, final CurrencyTableRepository currencyTableRepository,
                                     final CurrencyTableSearchRepository currencyTableSearchRepository) {
        this.currencyTableMapper = currencyTableMapper;
        this.currencyTableRepository = currencyTableRepository;
        this.currencyTableSearchRepository = currencyTableSearchRepository;
    }

    /**
     * Save an entity.
     *
     * @param entities entity to save.
     * @return the persisted entity.
     */
    @Override
    public List<CurrencyTableDTO> save(final List<CurrencyTableDTO> entities) {
        return currencyTableMapper.toDto(currencyTableRepository.saveAll(currencyTableMapper.toEntity(entities)));
    }

    /**
     * The above call only persists entities to the relations db repository for efficiency sake. Therefore to have it all in an index one needs to call this function
     */
    @Override
    public void index(final List<CurrencyTableDTO> entities) {

        currencyTableSearchRepository.saveAll(currencyTableMapper.toEntity(entities));
    }
}
