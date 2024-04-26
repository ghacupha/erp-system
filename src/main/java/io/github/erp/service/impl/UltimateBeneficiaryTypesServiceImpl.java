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

import io.github.erp.domain.UltimateBeneficiaryTypes;
import io.github.erp.repository.UltimateBeneficiaryTypesRepository;
import io.github.erp.repository.search.UltimateBeneficiaryTypesSearchRepository;
import io.github.erp.service.UltimateBeneficiaryTypesService;
import io.github.erp.service.dto.UltimateBeneficiaryTypesDTO;
import io.github.erp.service.mapper.UltimateBeneficiaryTypesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UltimateBeneficiaryTypes}.
 */
@Service
@Transactional
public class UltimateBeneficiaryTypesServiceImpl implements UltimateBeneficiaryTypesService {

    private final Logger log = LoggerFactory.getLogger(UltimateBeneficiaryTypesServiceImpl.class);

    private final UltimateBeneficiaryTypesRepository ultimateBeneficiaryTypesRepository;

    private final UltimateBeneficiaryTypesMapper ultimateBeneficiaryTypesMapper;

    private final UltimateBeneficiaryTypesSearchRepository ultimateBeneficiaryTypesSearchRepository;

    public UltimateBeneficiaryTypesServiceImpl(
        UltimateBeneficiaryTypesRepository ultimateBeneficiaryTypesRepository,
        UltimateBeneficiaryTypesMapper ultimateBeneficiaryTypesMapper,
        UltimateBeneficiaryTypesSearchRepository ultimateBeneficiaryTypesSearchRepository
    ) {
        this.ultimateBeneficiaryTypesRepository = ultimateBeneficiaryTypesRepository;
        this.ultimateBeneficiaryTypesMapper = ultimateBeneficiaryTypesMapper;
        this.ultimateBeneficiaryTypesSearchRepository = ultimateBeneficiaryTypesSearchRepository;
    }

    @Override
    public UltimateBeneficiaryTypesDTO save(UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO) {
        log.debug("Request to save UltimateBeneficiaryTypes : {}", ultimateBeneficiaryTypesDTO);
        UltimateBeneficiaryTypes ultimateBeneficiaryTypes = ultimateBeneficiaryTypesMapper.toEntity(ultimateBeneficiaryTypesDTO);
        ultimateBeneficiaryTypes = ultimateBeneficiaryTypesRepository.save(ultimateBeneficiaryTypes);
        UltimateBeneficiaryTypesDTO result = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);
        ultimateBeneficiaryTypesSearchRepository.save(ultimateBeneficiaryTypes);
        return result;
    }

    @Override
    public Optional<UltimateBeneficiaryTypesDTO> partialUpdate(UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO) {
        log.debug("Request to partially update UltimateBeneficiaryTypes : {}", ultimateBeneficiaryTypesDTO);

        return ultimateBeneficiaryTypesRepository
            .findById(ultimateBeneficiaryTypesDTO.getId())
            .map(existingUltimateBeneficiaryTypes -> {
                ultimateBeneficiaryTypesMapper.partialUpdate(existingUltimateBeneficiaryTypes, ultimateBeneficiaryTypesDTO);

                return existingUltimateBeneficiaryTypes;
            })
            .map(ultimateBeneficiaryTypesRepository::save)
            .map(savedUltimateBeneficiaryTypes -> {
                ultimateBeneficiaryTypesSearchRepository.save(savedUltimateBeneficiaryTypes);

                return savedUltimateBeneficiaryTypes;
            })
            .map(ultimateBeneficiaryTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UltimateBeneficiaryTypesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UltimateBeneficiaryTypes");
        return ultimateBeneficiaryTypesRepository.findAll(pageable).map(ultimateBeneficiaryTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UltimateBeneficiaryTypesDTO> findOne(Long id) {
        log.debug("Request to get UltimateBeneficiaryTypes : {}", id);
        return ultimateBeneficiaryTypesRepository.findById(id).map(ultimateBeneficiaryTypesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UltimateBeneficiaryTypes : {}", id);
        ultimateBeneficiaryTypesRepository.deleteById(id);
        ultimateBeneficiaryTypesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UltimateBeneficiaryTypesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UltimateBeneficiaryTypes for query {}", query);
        return ultimateBeneficiaryTypesSearchRepository.search(query, pageable).map(ultimateBeneficiaryTypesMapper::toDto);
    }
}
