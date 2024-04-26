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

import io.github.erp.domain.Invoice;
import io.github.erp.repository.InvoiceRepository;
import io.github.erp.repository.search.InvoiceSearchRepository;
import io.github.erp.service.InvoiceService;
import io.github.erp.service.dto.InvoiceDTO;
import io.github.erp.service.mapper.InvoiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Invoice}.
 */
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    private final InvoiceSearchRepository invoiceSearchRepository;

    public InvoiceServiceImpl(
        InvoiceRepository invoiceRepository,
        InvoiceMapper invoiceMapper,
        InvoiceSearchRepository invoiceSearchRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.invoiceSearchRepository = invoiceSearchRepository;
    }

    @Override
    public InvoiceDTO save(InvoiceDTO invoiceDTO) {
        log.debug("Request to save Invoice : {}", invoiceDTO);
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        invoice = invoiceRepository.save(invoice);
        InvoiceDTO result = invoiceMapper.toDto(invoice);
        invoiceSearchRepository.save(invoice);
        return result;
    }

    @Override
    public Optional<InvoiceDTO> partialUpdate(InvoiceDTO invoiceDTO) {
        log.debug("Request to partially update Invoice : {}", invoiceDTO);

        return invoiceRepository
            .findById(invoiceDTO.getId())
            .map(existingInvoice -> {
                invoiceMapper.partialUpdate(existingInvoice, invoiceDTO);

                return existingInvoice;
            })
            .map(invoiceRepository::save)
            .map(savedInvoice -> {
                invoiceSearchRepository.save(savedInvoice);

                return savedInvoice;
            })
            .map(invoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll(pageable).map(invoiceMapper::toDto);
    }

    public Page<InvoiceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return invoiceRepository.findAllWithEagerRelationships(pageable).map(invoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceDTO> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findOneWithEagerRelationships(id).map(invoiceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
        invoiceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Invoices for query {}", query);
        return invoiceSearchRepository.search(query, pageable).map(invoiceMapper::toDto);
    }
}
