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

import io.github.erp.domain.Placeholder;
import io.github.erp.repository.PlaceholderRepository;
import io.github.erp.repository.search.PlaceholderSearchRepository;
import io.github.erp.service.PlaceholderService;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.mapper.PlaceholderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Placeholder}.
 */
@Service
@Transactional
public class PlaceholderServiceImpl implements PlaceholderService {

    private final Logger log = LoggerFactory.getLogger(PlaceholderServiceImpl.class);

    private final PlaceholderRepository placeholderRepository;

    private final PlaceholderMapper placeholderMapper;

    private final PlaceholderSearchRepository placeholderSearchRepository;

    public PlaceholderServiceImpl(
        PlaceholderRepository placeholderRepository,
        PlaceholderMapper placeholderMapper,
        PlaceholderSearchRepository placeholderSearchRepository
    ) {
        this.placeholderRepository = placeholderRepository;
        this.placeholderMapper = placeholderMapper;
        this.placeholderSearchRepository = placeholderSearchRepository;
    }

    @Override
    public PlaceholderDTO save(PlaceholderDTO placeholderDTO) {
        log.debug("Request to save Placeholder : {}", placeholderDTO);
        Placeholder placeholder = placeholderMapper.toEntity(placeholderDTO);
        placeholder = placeholderRepository.save(placeholder);
        PlaceholderDTO result = placeholderMapper.toDto(placeholder);
        placeholderSearchRepository.save(placeholder);
        return result;
    }

    @Override
    public Optional<PlaceholderDTO> partialUpdate(PlaceholderDTO placeholderDTO) {
        log.debug("Request to partially update Placeholder : {}", placeholderDTO);

        return placeholderRepository
            .findById(placeholderDTO.getId())
            .map(existingPlaceholder -> {
                placeholderMapper.partialUpdate(existingPlaceholder, placeholderDTO);

                return existingPlaceholder;
            })
            .map(placeholderRepository::save)
            .map(savedPlaceholder -> {
                placeholderSearchRepository.save(savedPlaceholder);

                return savedPlaceholder;
            })
            .map(placeholderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaceholderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Placeholders");
        return placeholderRepository.findAll(pageable).map(placeholderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlaceholderDTO> findOne(Long id) {
        log.debug("Request to get Placeholder : {}", id);
        return placeholderRepository.findById(id).map(placeholderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Placeholder : {}", id);
        placeholderRepository.deleteById(id);
        placeholderSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaceholderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Placeholders for query {}", query);
        return placeholderSearchRepository.search(query, pageable).map(placeholderMapper::toDto);
    }
}
