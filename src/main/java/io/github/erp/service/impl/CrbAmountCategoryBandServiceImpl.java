package io.github.erp.service.impl;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.CrbAmountCategoryBand;
import io.github.erp.repository.CrbAmountCategoryBandRepository;
import io.github.erp.repository.search.CrbAmountCategoryBandSearchRepository;
import io.github.erp.service.CrbAmountCategoryBandService;
import io.github.erp.service.dto.CrbAmountCategoryBandDTO;
import io.github.erp.service.mapper.CrbAmountCategoryBandMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbAmountCategoryBand}.
 */
@Service
@Transactional
public class CrbAmountCategoryBandServiceImpl implements CrbAmountCategoryBandService {

    private final Logger log = LoggerFactory.getLogger(CrbAmountCategoryBandServiceImpl.class);

    private final CrbAmountCategoryBandRepository crbAmountCategoryBandRepository;

    private final CrbAmountCategoryBandMapper crbAmountCategoryBandMapper;

    private final CrbAmountCategoryBandSearchRepository crbAmountCategoryBandSearchRepository;

    public CrbAmountCategoryBandServiceImpl(
        CrbAmountCategoryBandRepository crbAmountCategoryBandRepository,
        CrbAmountCategoryBandMapper crbAmountCategoryBandMapper,
        CrbAmountCategoryBandSearchRepository crbAmountCategoryBandSearchRepository
    ) {
        this.crbAmountCategoryBandRepository = crbAmountCategoryBandRepository;
        this.crbAmountCategoryBandMapper = crbAmountCategoryBandMapper;
        this.crbAmountCategoryBandSearchRepository = crbAmountCategoryBandSearchRepository;
    }

    @Override
    public CrbAmountCategoryBandDTO save(CrbAmountCategoryBandDTO crbAmountCategoryBandDTO) {
        log.debug("Request to save CrbAmountCategoryBand : {}", crbAmountCategoryBandDTO);
        CrbAmountCategoryBand crbAmountCategoryBand = crbAmountCategoryBandMapper.toEntity(crbAmountCategoryBandDTO);
        crbAmountCategoryBand = crbAmountCategoryBandRepository.save(crbAmountCategoryBand);
        CrbAmountCategoryBandDTO result = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);
        crbAmountCategoryBandSearchRepository.save(crbAmountCategoryBand);
        return result;
    }

    @Override
    public Optional<CrbAmountCategoryBandDTO> partialUpdate(CrbAmountCategoryBandDTO crbAmountCategoryBandDTO) {
        log.debug("Request to partially update CrbAmountCategoryBand : {}", crbAmountCategoryBandDTO);

        return crbAmountCategoryBandRepository
            .findById(crbAmountCategoryBandDTO.getId())
            .map(existingCrbAmountCategoryBand -> {
                crbAmountCategoryBandMapper.partialUpdate(existingCrbAmountCategoryBand, crbAmountCategoryBandDTO);

                return existingCrbAmountCategoryBand;
            })
            .map(crbAmountCategoryBandRepository::save)
            .map(savedCrbAmountCategoryBand -> {
                crbAmountCategoryBandSearchRepository.save(savedCrbAmountCategoryBand);

                return savedCrbAmountCategoryBand;
            })
            .map(crbAmountCategoryBandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAmountCategoryBandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbAmountCategoryBands");
        return crbAmountCategoryBandRepository.findAll(pageable).map(crbAmountCategoryBandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbAmountCategoryBandDTO> findOne(Long id) {
        log.debug("Request to get CrbAmountCategoryBand : {}", id);
        return crbAmountCategoryBandRepository.findById(id).map(crbAmountCategoryBandMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbAmountCategoryBand : {}", id);
        crbAmountCategoryBandRepository.deleteById(id);
        crbAmountCategoryBandSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAmountCategoryBandDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbAmountCategoryBands for query {}", query);
        return crbAmountCategoryBandSearchRepository.search(query, pageable).map(crbAmountCategoryBandMapper::toDto);
    }
}
