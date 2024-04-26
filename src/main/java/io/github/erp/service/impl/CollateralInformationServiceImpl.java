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

import io.github.erp.domain.CollateralInformation;
import io.github.erp.repository.CollateralInformationRepository;
import io.github.erp.repository.search.CollateralInformationSearchRepository;
import io.github.erp.service.CollateralInformationService;
import io.github.erp.service.dto.CollateralInformationDTO;
import io.github.erp.service.mapper.CollateralInformationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CollateralInformation}.
 */
@Service
@Transactional
public class CollateralInformationServiceImpl implements CollateralInformationService {

    private final Logger log = LoggerFactory.getLogger(CollateralInformationServiceImpl.class);

    private final CollateralInformationRepository collateralInformationRepository;

    private final CollateralInformationMapper collateralInformationMapper;

    private final CollateralInformationSearchRepository collateralInformationSearchRepository;

    public CollateralInformationServiceImpl(
        CollateralInformationRepository collateralInformationRepository,
        CollateralInformationMapper collateralInformationMapper,
        CollateralInformationSearchRepository collateralInformationSearchRepository
    ) {
        this.collateralInformationRepository = collateralInformationRepository;
        this.collateralInformationMapper = collateralInformationMapper;
        this.collateralInformationSearchRepository = collateralInformationSearchRepository;
    }

    @Override
    public CollateralInformationDTO save(CollateralInformationDTO collateralInformationDTO) {
        log.debug("Request to save CollateralInformation : {}", collateralInformationDTO);
        CollateralInformation collateralInformation = collateralInformationMapper.toEntity(collateralInformationDTO);
        collateralInformation = collateralInformationRepository.save(collateralInformation);
        CollateralInformationDTO result = collateralInformationMapper.toDto(collateralInformation);
        collateralInformationSearchRepository.save(collateralInformation);
        return result;
    }

    @Override
    public Optional<CollateralInformationDTO> partialUpdate(CollateralInformationDTO collateralInformationDTO) {
        log.debug("Request to partially update CollateralInformation : {}", collateralInformationDTO);

        return collateralInformationRepository
            .findById(collateralInformationDTO.getId())
            .map(existingCollateralInformation -> {
                collateralInformationMapper.partialUpdate(existingCollateralInformation, collateralInformationDTO);

                return existingCollateralInformation;
            })
            .map(collateralInformationRepository::save)
            .map(savedCollateralInformation -> {
                collateralInformationSearchRepository.save(savedCollateralInformation);

                return savedCollateralInformation;
            })
            .map(collateralInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CollateralInformationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CollateralInformations");
        return collateralInformationRepository.findAll(pageable).map(collateralInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollateralInformationDTO> findOne(Long id) {
        log.debug("Request to get CollateralInformation : {}", id);
        return collateralInformationRepository.findById(id).map(collateralInformationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CollateralInformation : {}", id);
        collateralInformationRepository.deleteById(id);
        collateralInformationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CollateralInformationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CollateralInformations for query {}", query);
        return collateralInformationSearchRepository.search(query, pageable).map(collateralInformationMapper::toDto);
    }
}
