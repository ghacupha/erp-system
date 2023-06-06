package io.github.erp.internal.service;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.5
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
import io.github.erp.internal.framework.BatchService;
import io.github.erp.repository.FixedAssetAcquisitionRepository;
import io.github.erp.repository.search.FixedAssetAcquisitionSearchRepository;
import io.github.erp.service.dto.FixedAssetAcquisitionDTO;
import io.github.erp.service.mapper.FixedAssetAcquisitionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("fixedAssetAcquisitionBatchService")
public class FixedAssetAcquisitionBatchService implements BatchService<FixedAssetAcquisitionDTO> {

    private final FixedAssetAcquisitionMapper mapper;
    private final FixedAssetAcquisitionRepository repository;
    private final FixedAssetAcquisitionSearchRepository searchRepository;

    public FixedAssetAcquisitionBatchService(FixedAssetAcquisitionMapper mapper, FixedAssetAcquisitionRepository repository, FixedAssetAcquisitionSearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<FixedAssetAcquisitionDTO> save(List<FixedAssetAcquisitionDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<FixedAssetAcquisitionDTO> entities) {

        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
