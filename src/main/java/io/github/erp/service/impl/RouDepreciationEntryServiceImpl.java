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

import io.github.erp.domain.RouDepreciationEntry;
import io.github.erp.repository.RouDepreciationEntryRepository;
import io.github.erp.repository.search.RouDepreciationEntrySearchRepository;
import io.github.erp.service.RouDepreciationEntryService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.mapper.RouDepreciationEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouDepreciationEntry}.
 */
@Service
@Transactional
public class RouDepreciationEntryServiceImpl implements RouDepreciationEntryService {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationEntryServiceImpl.class);

    private final RouDepreciationEntryRepository rouDepreciationEntryRepository;

    private final RouDepreciationEntryMapper rouDepreciationEntryMapper;

    private final RouDepreciationEntrySearchRepository rouDepreciationEntrySearchRepository;

    public RouDepreciationEntryServiceImpl(
        RouDepreciationEntryRepository rouDepreciationEntryRepository,
        RouDepreciationEntryMapper rouDepreciationEntryMapper,
        RouDepreciationEntrySearchRepository rouDepreciationEntrySearchRepository
    ) {
        this.rouDepreciationEntryRepository = rouDepreciationEntryRepository;
        this.rouDepreciationEntryMapper = rouDepreciationEntryMapper;
        this.rouDepreciationEntrySearchRepository = rouDepreciationEntrySearchRepository;
    }

    @Override
    public RouDepreciationEntryDTO save(RouDepreciationEntryDTO rouDepreciationEntryDTO) {
        log.debug("Request to save RouDepreciationEntry : {}", rouDepreciationEntryDTO);
        RouDepreciationEntry rouDepreciationEntry = rouDepreciationEntryMapper.toEntity(rouDepreciationEntryDTO);
        rouDepreciationEntry = rouDepreciationEntryRepository.save(rouDepreciationEntry);
        RouDepreciationEntryDTO result = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);
        rouDepreciationEntrySearchRepository.save(rouDepreciationEntry);
        return result;
    }

    @Override
    public Optional<RouDepreciationEntryDTO> partialUpdate(RouDepreciationEntryDTO rouDepreciationEntryDTO) {
        log.debug("Request to partially update RouDepreciationEntry : {}", rouDepreciationEntryDTO);

        return rouDepreciationEntryRepository
            .findById(rouDepreciationEntryDTO.getId())
            .map(existingRouDepreciationEntry -> {
                rouDepreciationEntryMapper.partialUpdate(existingRouDepreciationEntry, rouDepreciationEntryDTO);

                return existingRouDepreciationEntry;
            })
            .map(rouDepreciationEntryRepository::save)
            .map(savedRouDepreciationEntry -> {
                rouDepreciationEntrySearchRepository.save(savedRouDepreciationEntry);

                return savedRouDepreciationEntry;
            })
            .map(rouDepreciationEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouDepreciationEntries");
        return rouDepreciationEntryRepository.findAll(pageable).map(rouDepreciationEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouDepreciationEntryDTO> findOne(Long id) {
        log.debug("Request to get RouDepreciationEntry : {}", id);
        return rouDepreciationEntryRepository.findById(id).map(rouDepreciationEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouDepreciationEntry : {}", id);
        rouDepreciationEntryRepository.deleteById(id);
        rouDepreciationEntrySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouDepreciationEntries for query {}", query);
        return rouDepreciationEntrySearchRepository.search(query, pageable).map(rouDepreciationEntryMapper::toDto);
    }
}
