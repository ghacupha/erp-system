package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.5-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.PaymentCalculation;
import io.github.erp.repository.PaymentCalculationRepository;
import io.github.erp.repository.search.PaymentCalculationSearchRepository;
import io.github.erp.service.PaymentCalculationService;
import io.github.erp.service.dto.PaymentCalculationDTO;
import io.github.erp.service.mapper.PaymentCalculationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaymentCalculation}.
 */
@Service
@Transactional
public class PaymentCalculationServiceImpl implements PaymentCalculationService {

    private final Logger log = LoggerFactory.getLogger(PaymentCalculationServiceImpl.class);

    private final PaymentCalculationRepository paymentCalculationRepository;

    private final PaymentCalculationMapper paymentCalculationMapper;

    private final PaymentCalculationSearchRepository paymentCalculationSearchRepository;

    public PaymentCalculationServiceImpl(
        PaymentCalculationRepository paymentCalculationRepository,
        PaymentCalculationMapper paymentCalculationMapper,
        PaymentCalculationSearchRepository paymentCalculationSearchRepository
    ) {
        this.paymentCalculationRepository = paymentCalculationRepository;
        this.paymentCalculationMapper = paymentCalculationMapper;
        this.paymentCalculationSearchRepository = paymentCalculationSearchRepository;
    }

    @Override
    public PaymentCalculationDTO save(PaymentCalculationDTO paymentCalculationDTO) {
        log.debug("Request to save PaymentCalculation : {}", paymentCalculationDTO);
        PaymentCalculation paymentCalculation = paymentCalculationMapper.toEntity(paymentCalculationDTO);
        paymentCalculation = paymentCalculationRepository.save(paymentCalculation);
        PaymentCalculationDTO result = paymentCalculationMapper.toDto(paymentCalculation);
        paymentCalculationSearchRepository.save(paymentCalculation);
        return result;
    }

    @Override
    public Optional<PaymentCalculationDTO> partialUpdate(PaymentCalculationDTO paymentCalculationDTO) {
        log.debug("Request to partially update PaymentCalculation : {}", paymentCalculationDTO);

        return paymentCalculationRepository
            .findById(paymentCalculationDTO.getId())
            .map(existingPaymentCalculation -> {
                paymentCalculationMapper.partialUpdate(existingPaymentCalculation, paymentCalculationDTO);

                return existingPaymentCalculation;
            })
            .map(paymentCalculationRepository::save)
            .map(savedPaymentCalculation -> {
                paymentCalculationSearchRepository.save(savedPaymentCalculation);

                return savedPaymentCalculation;
            })
            .map(paymentCalculationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentCalculationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentCalculations");
        return paymentCalculationRepository.findAll(pageable).map(paymentCalculationMapper::toDto);
    }

    public Page<PaymentCalculationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paymentCalculationRepository.findAllWithEagerRelationships(pageable).map(paymentCalculationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentCalculationDTO> findOne(Long id) {
        log.debug("Request to get PaymentCalculation : {}", id);
        return paymentCalculationRepository.findOneWithEagerRelationships(id).map(paymentCalculationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentCalculation : {}", id);
        paymentCalculationRepository.deleteById(id);
        paymentCalculationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentCalculationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentCalculations for query {}", query);
        return paymentCalculationSearchRepository.search(query, pageable).map(paymentCalculationMapper::toDto);
    }
}
