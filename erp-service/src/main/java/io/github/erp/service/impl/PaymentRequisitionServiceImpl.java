package io.github.erp.service.impl;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.service.PaymentRequisitionService;
import io.github.erp.domain.PaymentRequisition;
import io.github.erp.repository.PaymentRequisitionRepository;
import io.github.erp.repository.search.PaymentRequisitionSearchRepository;
import io.github.erp.service.dto.PaymentRequisitionDTO;
import io.github.erp.service.mapper.PaymentRequisitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

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

    public PaymentRequisitionServiceImpl(PaymentRequisitionRepository paymentRequisitionRepository, PaymentRequisitionMapper paymentRequisitionMapper, PaymentRequisitionSearchRepository paymentRequisitionSearchRepository) {
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
    @Transactional(readOnly = true)
    public Page<PaymentRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentRequisitions");
        return paymentRequisitionRepository.findAll(pageable)
            .map(paymentRequisitionMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentRequisitionDTO> findOne(Long id) {
        log.debug("Request to get PaymentRequisition : {}", id);
        return paymentRequisitionRepository.findById(id)
            .map(paymentRequisitionMapper::toDto);
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
        return paymentRequisitionSearchRepository.search(queryStringQuery(query), pageable)
            .map(paymentRequisitionMapper::toDto);
    }
}