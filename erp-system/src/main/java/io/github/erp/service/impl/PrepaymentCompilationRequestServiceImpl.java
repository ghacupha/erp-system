package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.PrepaymentCompilationRequest;
import io.github.erp.repository.PrepaymentCompilationRequestRepository;
import io.github.erp.repository.search.PrepaymentCompilationRequestSearchRepository;
import io.github.erp.service.PrepaymentCompilationRequestService;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import io.github.erp.service.mapper.PrepaymentCompilationRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PrepaymentCompilationRequest}.
 */
@Service
@Transactional
public class PrepaymentCompilationRequestServiceImpl implements PrepaymentCompilationRequestService {

    private final Logger log = LoggerFactory.getLogger(PrepaymentCompilationRequestServiceImpl.class);

    private final PrepaymentCompilationRequestRepository prepaymentCompilationRequestRepository;

    private final PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper;

    private final PrepaymentCompilationRequestSearchRepository prepaymentCompilationRequestSearchRepository;

    public PrepaymentCompilationRequestServiceImpl(
        PrepaymentCompilationRequestRepository prepaymentCompilationRequestRepository,
        PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper,
        PrepaymentCompilationRequestSearchRepository prepaymentCompilationRequestSearchRepository
    ) {
        this.prepaymentCompilationRequestRepository = prepaymentCompilationRequestRepository;
        this.prepaymentCompilationRequestMapper = prepaymentCompilationRequestMapper;
        this.prepaymentCompilationRequestSearchRepository = prepaymentCompilationRequestSearchRepository;
    }

    @Override
    public PrepaymentCompilationRequestDTO save(PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO) {
        log.debug("Request to save PrepaymentCompilationRequest : {}", prepaymentCompilationRequestDTO);
        PrepaymentCompilationRequest prepaymentCompilationRequest = prepaymentCompilationRequestMapper.toEntity(
            prepaymentCompilationRequestDTO
        );
        prepaymentCompilationRequest = prepaymentCompilationRequestRepository.save(prepaymentCompilationRequest);
        PrepaymentCompilationRequestDTO result = prepaymentCompilationRequestMapper.toDto(prepaymentCompilationRequest);
        prepaymentCompilationRequestSearchRepository.save(prepaymentCompilationRequest);
        return result;
    }

    @Override
    public Optional<PrepaymentCompilationRequestDTO> partialUpdate(PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO) {
        log.debug("Request to partially update PrepaymentCompilationRequest : {}", prepaymentCompilationRequestDTO);

        return prepaymentCompilationRequestRepository
            .findById(prepaymentCompilationRequestDTO.getId())
            .map(existingPrepaymentCompilationRequest -> {
                prepaymentCompilationRequestMapper.partialUpdate(existingPrepaymentCompilationRequest, prepaymentCompilationRequestDTO);

                return existingPrepaymentCompilationRequest;
            })
            .map(prepaymentCompilationRequestRepository::save)
            .map(savedPrepaymentCompilationRequest -> {
                prepaymentCompilationRequestSearchRepository.save(savedPrepaymentCompilationRequest);

                return savedPrepaymentCompilationRequest;
            })
            .map(prepaymentCompilationRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentCompilationRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentCompilationRequests");
        return prepaymentCompilationRequestRepository.findAll(pageable).map(prepaymentCompilationRequestMapper::toDto);
    }

    public Page<PrepaymentCompilationRequestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prepaymentCompilationRequestRepository
            .findAllWithEagerRelationships(pageable)
            .map(prepaymentCompilationRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentCompilationRequestDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentCompilationRequest : {}", id);
        return prepaymentCompilationRequestRepository.findOneWithEagerRelationships(id).map(prepaymentCompilationRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentCompilationRequest : {}", id);
        prepaymentCompilationRequestRepository.deleteById(id);
        prepaymentCompilationRequestSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentCompilationRequestDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentCompilationRequests for query {}", query);
        return prepaymentCompilationRequestSearchRepository.search(query, pageable).map(prepaymentCompilationRequestMapper::toDto);
    }
}
