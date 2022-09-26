package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 1 (Caleb Series) Server ver 0.1.1-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.PaymentRequisition;
import io.github.erp.repository.PaymentRequisitionRepository;
import io.github.erp.repository.search.PaymentRequisitionSearchRepository;
import io.github.erp.service.PaymentRequisitionService;
import io.github.erp.service.dto.PaymentRequisitionDTO;
import io.github.erp.service.mapper.PaymentRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaymentRequisition}.
 */
@Service
@Transactional
public class PaymentRequisitionServiceImpl implements PaymentRequisitionService {

    private final Logger log = LoggerFactory.getLogger(PaymentRequisitionServiceImpl.class);

    private final PaymentRequisitionRepository paymentRequisitionRepository;

    private final PaymentRequisitionMapper paymentRequisitionMapper;

    private final PaymentRequisitionSearchRepository paymentRequisitionSearchRepository;

    public PaymentRequisitionServiceImpl(
        PaymentRequisitionRepository paymentRequisitionRepository,
        PaymentRequisitionMapper paymentRequisitionMapper,
        PaymentRequisitionSearchRepository paymentRequisitionSearchRepository
    ) {
        this.paymentRequisitionRepository = paymentRequisitionRepository;
        this.paymentRequisitionMapper = paymentRequisitionMapper;
        this.paymentRequisitionSearchRepository = paymentRequisitionSearchRepository;
    }

    @Override
    public PaymentRequisitionDTO save(PaymentRequisitionDTO paymentRequisitionDTO) {
        log.debug("Request to save PaymentRequisition : {}", paymentRequisitionDTO);
        PaymentRequisition paymentRequisition = paymentRequisitionMapper.toEntity(paymentRequisitionDTO);
        paymentRequisition = paymentRequisitionRepository.save(paymentRequisition);
        PaymentRequisitionDTO result = paymentRequisitionMapper.toDto(paymentRequisition);
        paymentRequisitionSearchRepository.save(paymentRequisition);
        return result;
    }

    @Override
    public Optional<PaymentRequisitionDTO> partialUpdate(PaymentRequisitionDTO paymentRequisitionDTO) {
        log.debug("Request to partially update PaymentRequisition : {}", paymentRequisitionDTO);

        return paymentRequisitionRepository
            .findById(paymentRequisitionDTO.getId())
            .map(existingPaymentRequisition -> {
                paymentRequisitionMapper.partialUpdate(existingPaymentRequisition, paymentRequisitionDTO);

                return existingPaymentRequisition;
            })
            .map(paymentRequisitionRepository::save)
            .map(savedPaymentRequisition -> {
                paymentRequisitionSearchRepository.save(savedPaymentRequisition);

                return savedPaymentRequisition;
            })
            .map(paymentRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentRequisitions");
        return paymentRequisitionRepository.findAll(pageable).map(paymentRequisitionMapper::toDto);
    }

    public Page<PaymentRequisitionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paymentRequisitionRepository.findAllWithEagerRelationships(pageable).map(paymentRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentRequisitionDTO> findOne(Long id) {
        log.debug("Request to get PaymentRequisition : {}", id);
        return paymentRequisitionRepository.findOneWithEagerRelationships(id).map(paymentRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentRequisition : {}", id);
        paymentRequisitionRepository.deleteById(id);
        paymentRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentRequisitions for query {}", query);
        return paymentRequisitionSearchRepository.search(query, pageable).map(paymentRequisitionMapper::toDto);
    }
}
