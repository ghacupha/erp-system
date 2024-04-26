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

import io.github.erp.domain.InterestCalcMethod;
import io.github.erp.repository.InterestCalcMethodRepository;
import io.github.erp.repository.search.InterestCalcMethodSearchRepository;
import io.github.erp.service.InterestCalcMethodService;
import io.github.erp.service.dto.InterestCalcMethodDTO;
import io.github.erp.service.mapper.InterestCalcMethodMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InterestCalcMethod}.
 */
@Service
@Transactional
public class InterestCalcMethodServiceImpl implements InterestCalcMethodService {

    private final Logger log = LoggerFactory.getLogger(InterestCalcMethodServiceImpl.class);

    private final InterestCalcMethodRepository interestCalcMethodRepository;

    private final InterestCalcMethodMapper interestCalcMethodMapper;

    private final InterestCalcMethodSearchRepository interestCalcMethodSearchRepository;

    public InterestCalcMethodServiceImpl(
        InterestCalcMethodRepository interestCalcMethodRepository,
        InterestCalcMethodMapper interestCalcMethodMapper,
        InterestCalcMethodSearchRepository interestCalcMethodSearchRepository
    ) {
        this.interestCalcMethodRepository = interestCalcMethodRepository;
        this.interestCalcMethodMapper = interestCalcMethodMapper;
        this.interestCalcMethodSearchRepository = interestCalcMethodSearchRepository;
    }

    @Override
    public InterestCalcMethodDTO save(InterestCalcMethodDTO interestCalcMethodDTO) {
        log.debug("Request to save InterestCalcMethod : {}", interestCalcMethodDTO);
        InterestCalcMethod interestCalcMethod = interestCalcMethodMapper.toEntity(interestCalcMethodDTO);
        interestCalcMethod = interestCalcMethodRepository.save(interestCalcMethod);
        InterestCalcMethodDTO result = interestCalcMethodMapper.toDto(interestCalcMethod);
        interestCalcMethodSearchRepository.save(interestCalcMethod);
        return result;
    }

    @Override
    public Optional<InterestCalcMethodDTO> partialUpdate(InterestCalcMethodDTO interestCalcMethodDTO) {
        log.debug("Request to partially update InterestCalcMethod : {}", interestCalcMethodDTO);

        return interestCalcMethodRepository
            .findById(interestCalcMethodDTO.getId())
            .map(existingInterestCalcMethod -> {
                interestCalcMethodMapper.partialUpdate(existingInterestCalcMethod, interestCalcMethodDTO);

                return existingInterestCalcMethod;
            })
            .map(interestCalcMethodRepository::save)
            .map(savedInterestCalcMethod -> {
                interestCalcMethodSearchRepository.save(savedInterestCalcMethod);

                return savedInterestCalcMethod;
            })
            .map(interestCalcMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterestCalcMethodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InterestCalcMethods");
        return interestCalcMethodRepository.findAll(pageable).map(interestCalcMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InterestCalcMethodDTO> findOne(Long id) {
        log.debug("Request to get InterestCalcMethod : {}", id);
        return interestCalcMethodRepository.findById(id).map(interestCalcMethodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InterestCalcMethod : {}", id);
        interestCalcMethodRepository.deleteById(id);
        interestCalcMethodSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterestCalcMethodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InterestCalcMethods for query {}", query);
        return interestCalcMethodSearchRepository.search(query, pageable).map(interestCalcMethodMapper::toDto);
    }
}
