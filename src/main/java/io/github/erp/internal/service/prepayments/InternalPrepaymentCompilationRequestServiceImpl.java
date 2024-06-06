package io.github.erp.internal.service.prepayments;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.PrepaymentCompilationRequest;
import io.github.erp.internal.repository.InternalPrepaymentAmortizationRepository;
import io.github.erp.internal.repository.InternalPrepaymentMarshallingRepository;
import io.github.erp.repository.PrepaymentCompilationRequestRepository;
import io.github.erp.repository.search.PrepaymentCompilationRequestSearchRepository;
import io.github.erp.service.PrepaymentAmortizationService;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import io.github.erp.service.mapper.PrepaymentCompilationRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("internalPrepaymentCompilationRequestService")
@Transactional
public class InternalPrepaymentCompilationRequestServiceImpl implements InternalPrepaymentCompilationRequestService {

    private final Logger log = LoggerFactory.getLogger(InternalPrepaymentCompilationRequestServiceImpl.class);

    private final InternalPrepaymentMarshallingRepository prepaymentMarshallingRepository;
    private final PrepaymentAmortizationService prepaymentAmortizationService;
    private final InternalPrepaymentAmortizationRepository prepaymentAmortizationRepository;
    private final PrepaymentCompilationRequestRepository prepaymentCompilationRequestRepository;
    private final PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper;
    private final PrepaymentCompilationRequestSearchRepository prepaymentCompilationRequestSearchRepository;

    public InternalPrepaymentCompilationRequestServiceImpl(InternalPrepaymentMarshallingRepository prepaymentMarshallingRepository, PrepaymentAmortizationService prepaymentAmortizationService, InternalPrepaymentAmortizationRepository prepaymentAmortizationRepository, PrepaymentCompilationRequestRepository prepaymentCompilationRequestRepository, PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper, PrepaymentCompilationRequestSearchRepository prepaymentCompilationRequestSearchRepository) {
        this.prepaymentMarshallingRepository = prepaymentMarshallingRepository;
        this.prepaymentAmortizationService = prepaymentAmortizationService;
        this.prepaymentAmortizationRepository = prepaymentAmortizationRepository;
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

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentCompilationRequestDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentCompilationRequest : {}", id);
        return prepaymentCompilationRequestRepository.findById(id).map(prepaymentCompilationRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {

        prepaymentCompilationRequestRepository.findById(id).ifPresent(compilationRequest -> {

            // Delete related amortization items
            prepaymentAmortizationRepository.findAllByPrepaymentCompilationRequest(compilationRequest).forEach(prepaymentAmortization -> {
                prepaymentAmortizationService.delete(prepaymentAmortization.getId());
            });

            // Update related marshalling items as not processed
            prepaymentMarshallingRepository.findPrepaymentMarshallingsByCompilationTokenEquals(compilationRequest.getCompilationToken()).forEach(marshalling -> {
                marshalling.setProcessed(false);
            });

            prepaymentCompilationRequestRepository.deleteById(id);
            prepaymentCompilationRequestSearchRepository.deleteById(id);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentCompilationRequestDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentCompilationRequests for query {}", query);
        return prepaymentCompilationRequestSearchRepository.search(query, pageable).map(prepaymentCompilationRequestMapper::toDto);
    }
}
