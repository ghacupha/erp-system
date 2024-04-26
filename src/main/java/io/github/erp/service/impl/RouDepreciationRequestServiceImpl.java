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

import io.github.erp.domain.RouDepreciationRequest;
import io.github.erp.repository.RouDepreciationRequestRepository;
import io.github.erp.repository.search.RouDepreciationRequestSearchRepository;
import io.github.erp.service.RouDepreciationRequestService;
import io.github.erp.service.dto.RouDepreciationRequestDTO;
import io.github.erp.service.mapper.RouDepreciationRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouDepreciationRequest}.
 */
@Service
@Transactional
public class RouDepreciationRequestServiceImpl implements RouDepreciationRequestService {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationRequestServiceImpl.class);

    private final RouDepreciationRequestRepository rouDepreciationRequestRepository;

    private final RouDepreciationRequestMapper rouDepreciationRequestMapper;

    private final RouDepreciationRequestSearchRepository rouDepreciationRequestSearchRepository;

    public RouDepreciationRequestServiceImpl(
        RouDepreciationRequestRepository rouDepreciationRequestRepository,
        RouDepreciationRequestMapper rouDepreciationRequestMapper,
        RouDepreciationRequestSearchRepository rouDepreciationRequestSearchRepository
    ) {
        this.rouDepreciationRequestRepository = rouDepreciationRequestRepository;
        this.rouDepreciationRequestMapper = rouDepreciationRequestMapper;
        this.rouDepreciationRequestSearchRepository = rouDepreciationRequestSearchRepository;
    }

    @Override
    public RouDepreciationRequestDTO save(RouDepreciationRequestDTO rouDepreciationRequestDTO) {
        log.debug("Request to save RouDepreciationRequest : {}", rouDepreciationRequestDTO);
        RouDepreciationRequest rouDepreciationRequest = rouDepreciationRequestMapper.toEntity(rouDepreciationRequestDTO);
        rouDepreciationRequest = rouDepreciationRequestRepository.save(rouDepreciationRequest);
        RouDepreciationRequestDTO result = rouDepreciationRequestMapper.toDto(rouDepreciationRequest);
        rouDepreciationRequestSearchRepository.save(rouDepreciationRequest);
        return result;
    }

    @Override
    public Optional<RouDepreciationRequestDTO> partialUpdate(RouDepreciationRequestDTO rouDepreciationRequestDTO) {
        log.debug("Request to partially update RouDepreciationRequest : {}", rouDepreciationRequestDTO);

        return rouDepreciationRequestRepository
            .findById(rouDepreciationRequestDTO.getId())
            .map(existingRouDepreciationRequest -> {
                rouDepreciationRequestMapper.partialUpdate(existingRouDepreciationRequest, rouDepreciationRequestDTO);

                return existingRouDepreciationRequest;
            })
            .map(rouDepreciationRequestRepository::save)
            .map(savedRouDepreciationRequest -> {
                rouDepreciationRequestSearchRepository.save(savedRouDepreciationRequest);

                return savedRouDepreciationRequest;
            })
            .map(rouDepreciationRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouDepreciationRequests");
        return rouDepreciationRequestRepository.findAll(pageable).map(rouDepreciationRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouDepreciationRequestDTO> findOne(Long id) {
        log.debug("Request to get RouDepreciationRequest : {}", id);
        return rouDepreciationRequestRepository.findById(id).map(rouDepreciationRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouDepreciationRequest : {}", id);
        rouDepreciationRequestRepository.deleteById(id);
        rouDepreciationRequestSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationRequestDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouDepreciationRequests for query {}", query);
        return rouDepreciationRequestSearchRepository.search(query, pageable).map(rouDepreciationRequestMapper::toDto);
    }
}
