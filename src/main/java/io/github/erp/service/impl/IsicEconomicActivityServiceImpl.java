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

import io.github.erp.domain.IsicEconomicActivity;
import io.github.erp.repository.IsicEconomicActivityRepository;
import io.github.erp.repository.search.IsicEconomicActivitySearchRepository;
import io.github.erp.service.IsicEconomicActivityService;
import io.github.erp.service.dto.IsicEconomicActivityDTO;
import io.github.erp.service.mapper.IsicEconomicActivityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IsicEconomicActivity}.
 */
@Service
@Transactional
public class IsicEconomicActivityServiceImpl implements IsicEconomicActivityService {

    private final Logger log = LoggerFactory.getLogger(IsicEconomicActivityServiceImpl.class);

    private final IsicEconomicActivityRepository isicEconomicActivityRepository;

    private final IsicEconomicActivityMapper isicEconomicActivityMapper;

    private final IsicEconomicActivitySearchRepository isicEconomicActivitySearchRepository;

    public IsicEconomicActivityServiceImpl(
        IsicEconomicActivityRepository isicEconomicActivityRepository,
        IsicEconomicActivityMapper isicEconomicActivityMapper,
        IsicEconomicActivitySearchRepository isicEconomicActivitySearchRepository
    ) {
        this.isicEconomicActivityRepository = isicEconomicActivityRepository;
        this.isicEconomicActivityMapper = isicEconomicActivityMapper;
        this.isicEconomicActivitySearchRepository = isicEconomicActivitySearchRepository;
    }

    @Override
    public IsicEconomicActivityDTO save(IsicEconomicActivityDTO isicEconomicActivityDTO) {
        log.debug("Request to save IsicEconomicActivity : {}", isicEconomicActivityDTO);
        IsicEconomicActivity isicEconomicActivity = isicEconomicActivityMapper.toEntity(isicEconomicActivityDTO);
        isicEconomicActivity = isicEconomicActivityRepository.save(isicEconomicActivity);
        IsicEconomicActivityDTO result = isicEconomicActivityMapper.toDto(isicEconomicActivity);
        isicEconomicActivitySearchRepository.save(isicEconomicActivity);
        return result;
    }

    @Override
    public Optional<IsicEconomicActivityDTO> partialUpdate(IsicEconomicActivityDTO isicEconomicActivityDTO) {
        log.debug("Request to partially update IsicEconomicActivity : {}", isicEconomicActivityDTO);

        return isicEconomicActivityRepository
            .findById(isicEconomicActivityDTO.getId())
            .map(existingIsicEconomicActivity -> {
                isicEconomicActivityMapper.partialUpdate(existingIsicEconomicActivity, isicEconomicActivityDTO);

                return existingIsicEconomicActivity;
            })
            .map(isicEconomicActivityRepository::save)
            .map(savedIsicEconomicActivity -> {
                isicEconomicActivitySearchRepository.save(savedIsicEconomicActivity);

                return savedIsicEconomicActivity;
            })
            .map(isicEconomicActivityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsicEconomicActivityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IsicEconomicActivities");
        return isicEconomicActivityRepository.findAll(pageable).map(isicEconomicActivityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IsicEconomicActivityDTO> findOne(Long id) {
        log.debug("Request to get IsicEconomicActivity : {}", id);
        return isicEconomicActivityRepository.findById(id).map(isicEconomicActivityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IsicEconomicActivity : {}", id);
        isicEconomicActivityRepository.deleteById(id);
        isicEconomicActivitySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsicEconomicActivityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IsicEconomicActivities for query {}", query);
        return isicEconomicActivitySearchRepository.search(query, pageable).map(isicEconomicActivityMapper::toDto);
    }
}
