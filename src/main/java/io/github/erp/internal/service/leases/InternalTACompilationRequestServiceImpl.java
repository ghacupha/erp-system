package io.github.erp.internal.service.leases;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.TACompilationRequest;
import io.github.erp.internal.repository.InternalTACompilationRequestRepository;
import io.github.erp.internal.service.applicationUser.CurrentUserContext;
import io.github.erp.repository.TACompilationRequestRepository;
import io.github.erp.repository.search.TACompilationRequestSearchRepository;
import io.github.erp.service.dto.TACompilationRequestDTO;
import io.github.erp.service.mapper.TACompilationRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TACompilationRequest}.
 */
@Service
@Transactional
public class InternalTACompilationRequestServiceImpl implements InternalTACompilationRequestService {

    private final Logger log = LoggerFactory.getLogger(InternalTACompilationRequestServiceImpl.class);

    private final InternalTACompilationRequestRepository tACompilationRequestRepository;

    private final TACompilationRequestMapper tACompilationRequestMapper;

    private final TACompilationRequestSearchRepository tACompilationRequestSearchRepository;

    public InternalTACompilationRequestServiceImpl(
        InternalTACompilationRequestRepository tACompilationRequestRepository,
        TACompilationRequestMapper tACompilationRequestMapper,
        TACompilationRequestSearchRepository tACompilationRequestSearchRepository
    ) {
        this.tACompilationRequestRepository = tACompilationRequestRepository;
        this.tACompilationRequestMapper = tACompilationRequestMapper;
        this.tACompilationRequestSearchRepository = tACompilationRequestSearchRepository;
    }

    @Override
    public TACompilationRequestDTO save(TACompilationRequestDTO tACompilationRequestDTO) {
        log.debug("Request to save TACompilationRequest : {}", tACompilationRequestDTO);
        TACompilationRequest tACompilationRequest = tACompilationRequestMapper.toEntity(tACompilationRequestDTO);

        tACompilationRequest.setInitiatedBy(CurrentUserContext.getCurrentUser());

        tACompilationRequest = tACompilationRequestRepository.save(tACompilationRequest);
        TACompilationRequestDTO result = tACompilationRequestMapper.toDto(tACompilationRequest);
        tACompilationRequestSearchRepository.save(tACompilationRequest);
        return result;
    }

    @Override
    public Optional<TACompilationRequestDTO> partialUpdate(TACompilationRequestDTO tACompilationRequestDTO) {
        log.debug("Request to partially update TACompilationRequest : {}", tACompilationRequestDTO);

        return tACompilationRequestRepository
            .findById(tACompilationRequestDTO.getId())
            .map(existingTACompilationRequest -> {
                tACompilationRequestMapper.partialUpdate(existingTACompilationRequest, tACompilationRequestDTO);

                return existingTACompilationRequest;
            })
            .map(tACompilationRequestRepository::save)
            .map(savedTACompilationRequest -> {
                tACompilationRequestSearchRepository.save(savedTACompilationRequest);

                return savedTACompilationRequest;
            })
            .map(tACompilationRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TACompilationRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TACompilationRequests");
        return tACompilationRequestRepository.findAll(pageable).map(tACompilationRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TACompilationRequestDTO> findOne(Long id) {
        log.debug("Request to get TACompilationRequest : {}", id);
        return tACompilationRequestRepository.findById(id).map(tACompilationRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TACompilationRequest : {}", id);
        tACompilationRequestRepository.deleteById(id);
        tACompilationRequestSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TACompilationRequestDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TACompilationRequests for query {}", query);
        return tACompilationRequestSearchRepository.search(query, pageable).map(tACompilationRequestMapper::toDto);
    }
}
