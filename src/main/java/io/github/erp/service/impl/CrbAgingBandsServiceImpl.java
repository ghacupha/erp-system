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

import io.github.erp.domain.CrbAgingBands;
import io.github.erp.repository.CrbAgingBandsRepository;
import io.github.erp.repository.search.CrbAgingBandsSearchRepository;
import io.github.erp.service.CrbAgingBandsService;
import io.github.erp.service.dto.CrbAgingBandsDTO;
import io.github.erp.service.mapper.CrbAgingBandsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbAgingBands}.
 */
@Service
@Transactional
public class CrbAgingBandsServiceImpl implements CrbAgingBandsService {

    private final Logger log = LoggerFactory.getLogger(CrbAgingBandsServiceImpl.class);

    private final CrbAgingBandsRepository crbAgingBandsRepository;

    private final CrbAgingBandsMapper crbAgingBandsMapper;

    private final CrbAgingBandsSearchRepository crbAgingBandsSearchRepository;

    public CrbAgingBandsServiceImpl(
        CrbAgingBandsRepository crbAgingBandsRepository,
        CrbAgingBandsMapper crbAgingBandsMapper,
        CrbAgingBandsSearchRepository crbAgingBandsSearchRepository
    ) {
        this.crbAgingBandsRepository = crbAgingBandsRepository;
        this.crbAgingBandsMapper = crbAgingBandsMapper;
        this.crbAgingBandsSearchRepository = crbAgingBandsSearchRepository;
    }

    @Override
    public CrbAgingBandsDTO save(CrbAgingBandsDTO crbAgingBandsDTO) {
        log.debug("Request to save CrbAgingBands : {}", crbAgingBandsDTO);
        CrbAgingBands crbAgingBands = crbAgingBandsMapper.toEntity(crbAgingBandsDTO);
        crbAgingBands = crbAgingBandsRepository.save(crbAgingBands);
        CrbAgingBandsDTO result = crbAgingBandsMapper.toDto(crbAgingBands);
        crbAgingBandsSearchRepository.save(crbAgingBands);
        return result;
    }

    @Override
    public Optional<CrbAgingBandsDTO> partialUpdate(CrbAgingBandsDTO crbAgingBandsDTO) {
        log.debug("Request to partially update CrbAgingBands : {}", crbAgingBandsDTO);

        return crbAgingBandsRepository
            .findById(crbAgingBandsDTO.getId())
            .map(existingCrbAgingBands -> {
                crbAgingBandsMapper.partialUpdate(existingCrbAgingBands, crbAgingBandsDTO);

                return existingCrbAgingBands;
            })
            .map(crbAgingBandsRepository::save)
            .map(savedCrbAgingBands -> {
                crbAgingBandsSearchRepository.save(savedCrbAgingBands);

                return savedCrbAgingBands;
            })
            .map(crbAgingBandsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAgingBandsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbAgingBands");
        return crbAgingBandsRepository.findAll(pageable).map(crbAgingBandsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbAgingBandsDTO> findOne(Long id) {
        log.debug("Request to get CrbAgingBands : {}", id);
        return crbAgingBandsRepository.findById(id).map(crbAgingBandsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbAgingBands : {}", id);
        crbAgingBandsRepository.deleteById(id);
        crbAgingBandsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAgingBandsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbAgingBands for query {}", query);
        return crbAgingBandsSearchRepository.search(query, pageable).map(crbAgingBandsMapper::toDto);
    }
}
