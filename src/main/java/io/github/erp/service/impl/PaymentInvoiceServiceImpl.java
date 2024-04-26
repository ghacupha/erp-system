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

import io.github.erp.domain.PaymentInvoice;
import io.github.erp.repository.PaymentInvoiceRepository;
import io.github.erp.repository.search.PaymentInvoiceSearchRepository;
import io.github.erp.service.PaymentInvoiceService;
import io.github.erp.service.dto.PaymentInvoiceDTO;
import io.github.erp.service.mapper.PaymentInvoiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaymentInvoice}.
 */
@Service
@Transactional
public class PaymentInvoiceServiceImpl implements PaymentInvoiceService {

    private final Logger log = LoggerFactory.getLogger(PaymentInvoiceServiceImpl.class);

    private final PaymentInvoiceRepository paymentInvoiceRepository;

    private final PaymentInvoiceMapper paymentInvoiceMapper;

    private final PaymentInvoiceSearchRepository paymentInvoiceSearchRepository;

    public PaymentInvoiceServiceImpl(
        PaymentInvoiceRepository paymentInvoiceRepository,
        PaymentInvoiceMapper paymentInvoiceMapper,
        PaymentInvoiceSearchRepository paymentInvoiceSearchRepository
    ) {
        this.paymentInvoiceRepository = paymentInvoiceRepository;
        this.paymentInvoiceMapper = paymentInvoiceMapper;
        this.paymentInvoiceSearchRepository = paymentInvoiceSearchRepository;
    }

    @Override
    public PaymentInvoiceDTO save(PaymentInvoiceDTO paymentInvoiceDTO) {
        log.debug("Request to save PaymentInvoice : {}", paymentInvoiceDTO);
        PaymentInvoice paymentInvoice = paymentInvoiceMapper.toEntity(paymentInvoiceDTO);
        paymentInvoice = paymentInvoiceRepository.save(paymentInvoice);
        PaymentInvoiceDTO result = paymentInvoiceMapper.toDto(paymentInvoice);
        paymentInvoiceSearchRepository.save(paymentInvoice);
        return result;
    }

    @Override
    public Optional<PaymentInvoiceDTO> partialUpdate(PaymentInvoiceDTO paymentInvoiceDTO) {
        log.debug("Request to partially update PaymentInvoice : {}", paymentInvoiceDTO);

        return paymentInvoiceRepository
            .findById(paymentInvoiceDTO.getId())
            .map(existingPaymentInvoice -> {
                paymentInvoiceMapper.partialUpdate(existingPaymentInvoice, paymentInvoiceDTO);

                return existingPaymentInvoice;
            })
            .map(paymentInvoiceRepository::save)
            .map(savedPaymentInvoice -> {
                paymentInvoiceSearchRepository.save(savedPaymentInvoice);

                return savedPaymentInvoice;
            })
            .map(paymentInvoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentInvoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentInvoices");
        return paymentInvoiceRepository.findAll(pageable).map(paymentInvoiceMapper::toDto);
    }

    public Page<PaymentInvoiceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paymentInvoiceRepository.findAllWithEagerRelationships(pageable).map(paymentInvoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentInvoiceDTO> findOne(Long id) {
        log.debug("Request to get PaymentInvoice : {}", id);
        return paymentInvoiceRepository.findOneWithEagerRelationships(id).map(paymentInvoiceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentInvoice : {}", id);
        paymentInvoiceRepository.deleteById(id);
        paymentInvoiceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentInvoiceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentInvoices for query {}", query);
        return paymentInvoiceSearchRepository.search(query, pageable).map(paymentInvoiceMapper::toDto);
    }
}
