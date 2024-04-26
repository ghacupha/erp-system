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
