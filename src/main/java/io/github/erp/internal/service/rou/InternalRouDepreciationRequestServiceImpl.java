package io.github.erp.internal.service.rou;

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
import io.github.erp.domain.RouDepreciationRequest;
import io.github.erp.domain.enumeration.depreciationProcessStatusTypes;
import io.github.erp.internal.repository.InternalRouDepreciationRequestRepository;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.repository.search.RouDepreciationRequestSearchRepository;
import io.github.erp.service.dto.RouDepreciationRequestDTO;
import io.github.erp.service.mapper.RouDepreciationRequestMapper;
import java.util.Optional;
import java.util.UUID;

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
public class InternalRouDepreciationRequestServiceImpl implements InternalRouDepreciationRequestService {

    private final Logger log = LoggerFactory.getLogger(InternalRouDepreciationRequestServiceImpl.class);

    private final InternalApplicationUserDetailService internalApplicationUserDetailService;

    private final InternalRouDepreciationRequestRepository rouDepreciationRequestRepository;

    private final RouDepreciationRequestMapper rouDepreciationRequestMapper;

    private final RouDepreciationRequestSearchRepository rouDepreciationRequestSearchRepository;

    private final InternalRouDepreciationEntryService internalRouDepreciationEntryService;

    public InternalRouDepreciationRequestServiceImpl(
        InternalApplicationUserDetailService internalApplicationUserDetailService,
        InternalRouDepreciationRequestRepository rouDepreciationRequestRepository,
        RouDepreciationRequestMapper rouDepreciationRequestMapper,
        RouDepreciationRequestSearchRepository rouDepreciationRequestSearchRepository,
        InternalRouDepreciationEntryService internalRouDepreciationEntryService) {
        this.internalApplicationUserDetailService = internalApplicationUserDetailService;
        this.rouDepreciationRequestRepository = rouDepreciationRequestRepository;
        this.rouDepreciationRequestMapper = rouDepreciationRequestMapper;
        this.rouDepreciationRequestSearchRepository = rouDepreciationRequestSearchRepository;
        this.internalRouDepreciationEntryService = internalRouDepreciationEntryService;
    }

    @Override
    public RouDepreciationRequestDTO save(RouDepreciationRequestDTO rouDepreciationRequestDTO) {
        log.debug("Request to save RouDepreciationRequest : {}", rouDepreciationRequestDTO);

        // Simply short-circuit a non-compliant request
        internalApplicationUserDetailService.getCurrentApplicationUser().ifPresent(rouDepreciationRequestDTO::setInitiatedBy);

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

    @Override
    public RouDepreciationRequestDTO saveIdentifier(RouDepreciationRequestDTO requestDTO, UUID batchJobIdentifier) {

        requestDTO.setBatchJobIdentifier(batchJobIdentifier);

        RouDepreciationRequest request = rouDepreciationRequestRepository.save(rouDepreciationRequestMapper.toEntity(requestDTO));
        rouDepreciationRequestSearchRepository.save(rouDepreciationRequestMapper.toEntity(requestDTO));

        return rouDepreciationRequestMapper.toDto(request);
    }

    @Override
    public RouDepreciationRequestDTO markRequestComplete(RouDepreciationRequestDTO completed) {
        completed.setDepreciationProcessStatus(depreciationProcessStatusTypes.COMPLETE);
        completed.setNumberOfEnumeratedItems(
            internalRouDepreciationEntryService.countProcessedItems(completed.getBatchJobIdentifier()).orElse(0)
        );

        RouDepreciationRequest request = rouDepreciationRequestRepository.save(rouDepreciationRequestMapper.toEntity(completed));
        rouDepreciationRequestSearchRepository.save(request);

        return rouDepreciationRequestMapper.toDto(request);
    }
}
