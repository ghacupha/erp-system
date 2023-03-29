package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.2-SNAPSHOT
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
