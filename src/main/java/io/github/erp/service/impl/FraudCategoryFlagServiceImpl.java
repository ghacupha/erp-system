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

import io.github.erp.domain.FraudCategoryFlag;
import io.github.erp.repository.FraudCategoryFlagRepository;
import io.github.erp.repository.search.FraudCategoryFlagSearchRepository;
import io.github.erp.service.FraudCategoryFlagService;
import io.github.erp.service.dto.FraudCategoryFlagDTO;
import io.github.erp.service.mapper.FraudCategoryFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FraudCategoryFlag}.
 */
@Service
@Transactional
public class FraudCategoryFlagServiceImpl implements FraudCategoryFlagService {

    private final Logger log = LoggerFactory.getLogger(FraudCategoryFlagServiceImpl.class);

    private final FraudCategoryFlagRepository fraudCategoryFlagRepository;

    private final FraudCategoryFlagMapper fraudCategoryFlagMapper;

    private final FraudCategoryFlagSearchRepository fraudCategoryFlagSearchRepository;

    public FraudCategoryFlagServiceImpl(
        FraudCategoryFlagRepository fraudCategoryFlagRepository,
        FraudCategoryFlagMapper fraudCategoryFlagMapper,
        FraudCategoryFlagSearchRepository fraudCategoryFlagSearchRepository
    ) {
        this.fraudCategoryFlagRepository = fraudCategoryFlagRepository;
        this.fraudCategoryFlagMapper = fraudCategoryFlagMapper;
        this.fraudCategoryFlagSearchRepository = fraudCategoryFlagSearchRepository;
    }

    @Override
    public FraudCategoryFlagDTO save(FraudCategoryFlagDTO fraudCategoryFlagDTO) {
        log.debug("Request to save FraudCategoryFlag : {}", fraudCategoryFlagDTO);
        FraudCategoryFlag fraudCategoryFlag = fraudCategoryFlagMapper.toEntity(fraudCategoryFlagDTO);
        fraudCategoryFlag = fraudCategoryFlagRepository.save(fraudCategoryFlag);
        FraudCategoryFlagDTO result = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);
        fraudCategoryFlagSearchRepository.save(fraudCategoryFlag);
        return result;
    }

    @Override
    public Optional<FraudCategoryFlagDTO> partialUpdate(FraudCategoryFlagDTO fraudCategoryFlagDTO) {
        log.debug("Request to partially update FraudCategoryFlag : {}", fraudCategoryFlagDTO);

        return fraudCategoryFlagRepository
            .findById(fraudCategoryFlagDTO.getId())
            .map(existingFraudCategoryFlag -> {
                fraudCategoryFlagMapper.partialUpdate(existingFraudCategoryFlag, fraudCategoryFlagDTO);

                return existingFraudCategoryFlag;
            })
            .map(fraudCategoryFlagRepository::save)
            .map(savedFraudCategoryFlag -> {
                fraudCategoryFlagSearchRepository.save(savedFraudCategoryFlag);

                return savedFraudCategoryFlag;
            })
            .map(fraudCategoryFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraudCategoryFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FraudCategoryFlags");
        return fraudCategoryFlagRepository.findAll(pageable).map(fraudCategoryFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FraudCategoryFlagDTO> findOne(Long id) {
        log.debug("Request to get FraudCategoryFlag : {}", id);
        return fraudCategoryFlagRepository.findById(id).map(fraudCategoryFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FraudCategoryFlag : {}", id);
        fraudCategoryFlagRepository.deleteById(id);
        fraudCategoryFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraudCategoryFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FraudCategoryFlags for query {}", query);
        return fraudCategoryFlagSearchRepository.search(query, pageable).map(fraudCategoryFlagMapper::toDto);
    }
}
