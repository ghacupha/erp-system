package io.github.erp.internal.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.framework.BatchService;
import io.github.erp.repository.FixedAssetDepreciationRepository;
import io.github.erp.repository.search.FixedAssetDepreciationSearchRepository;
import io.github.erp.service.dto.FixedAssetDepreciationDTO;
import io.github.erp.service.mapper.FixedAssetDepreciationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("fixedAssetDepreciationBatchService")
public class FixedAssetDepreciationBatchService implements BatchService<FixedAssetDepreciationDTO> {

    private final FixedAssetDepreciationMapper mapper;
    private final FixedAssetDepreciationRepository repository;
    private final FixedAssetDepreciationSearchRepository searchRepository;

    public FixedAssetDepreciationBatchService(FixedAssetDepreciationMapper mapper, FixedAssetDepreciationRepository repository, FixedAssetDepreciationSearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<FixedAssetDepreciationDTO> save(List<FixedAssetDepreciationDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<FixedAssetDepreciationDTO> entities) {

        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
