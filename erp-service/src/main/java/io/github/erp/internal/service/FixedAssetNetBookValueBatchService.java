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

import io.github.leassets.internal.framework.BatchService;
import io.github.leassets.repository.FixedAssetNetBookValueRepository;
import io.github.leassets.repository.search.FixedAssetNetBookValueSearchRepository;
import io.github.leassets.service.dto.FixedAssetNetBookValueDTO;
import io.github.leassets.service.mapper.FixedAssetNetBookValueMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("fixedAssetNetBookValueBatchService")
public class FixedAssetNetBookValueBatchService implements BatchService<FixedAssetNetBookValueDTO> {

    private final FixedAssetNetBookValueMapper mapper;
    private final FixedAssetNetBookValueRepository repository;
    private final FixedAssetNetBookValueSearchRepository searchRepository;

    public FixedAssetNetBookValueBatchService(FixedAssetNetBookValueMapper mapper, FixedAssetNetBookValueRepository repository, FixedAssetNetBookValueSearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<FixedAssetNetBookValueDTO> save(List<FixedAssetNetBookValueDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<FixedAssetNetBookValueDTO> entities) {

        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
