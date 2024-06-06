package io.github.erp.service.impl;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.PaymentCategory;
import io.github.erp.repository.PaymentCategoryRepository;
import io.github.erp.repository.search.PaymentCategorySearchRepository;
import io.github.erp.service.PaymentCategoryService;
import io.github.erp.service.dto.PaymentCategoryDTO;
import io.github.erp.service.mapper.PaymentCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaymentCategory}.
 */
@Service
@Transactional
public class PaymentCategoryServiceImpl implements PaymentCategoryService {

    private final Logger log = LoggerFactory.getLogger(PaymentCategoryServiceImpl.class);

    private final PaymentCategoryRepository paymentCategoryRepository;

    private final PaymentCategoryMapper paymentCategoryMapper;

    private final PaymentCategorySearchRepository paymentCategorySearchRepository;

    public PaymentCategoryServiceImpl(
        PaymentCategoryRepository paymentCategoryRepository,
        PaymentCategoryMapper paymentCategoryMapper,
        PaymentCategorySearchRepository paymentCategorySearchRepository
    ) {
        this.paymentCategoryRepository = paymentCategoryRepository;
        this.paymentCategoryMapper = paymentCategoryMapper;
        this.paymentCategorySearchRepository = paymentCategorySearchRepository;
    }

    @Override
    public PaymentCategoryDTO save(PaymentCategoryDTO paymentCategoryDTO) {
        log.debug("Request to save PaymentCategory : {}", paymentCategoryDTO);
        PaymentCategory paymentCategory = paymentCategoryMapper.toEntity(paymentCategoryDTO);
        paymentCategory = paymentCategoryRepository.save(paymentCategory);
        PaymentCategoryDTO result = paymentCategoryMapper.toDto(paymentCategory);
        paymentCategorySearchRepository.save(paymentCategory);
        return result;
    }

    @Override
    public Optional<PaymentCategoryDTO> partialUpdate(PaymentCategoryDTO paymentCategoryDTO) {
        log.debug("Request to partially update PaymentCategory : {}", paymentCategoryDTO);

        return paymentCategoryRepository
            .findById(paymentCategoryDTO.getId())
            .map(existingPaymentCategory -> {
                paymentCategoryMapper.partialUpdate(existingPaymentCategory, paymentCategoryDTO);

                return existingPaymentCategory;
            })
            .map(paymentCategoryRepository::save)
            .map(savedPaymentCategory -> {
                paymentCategorySearchRepository.save(savedPaymentCategory);

                return savedPaymentCategory;
            })
            .map(paymentCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentCategories");
        return paymentCategoryRepository.findAll(pageable).map(paymentCategoryMapper::toDto);
    }

    public Page<PaymentCategoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paymentCategoryRepository.findAllWithEagerRelationships(pageable).map(paymentCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentCategoryDTO> findOne(Long id) {
        log.debug("Request to get PaymentCategory : {}", id);
        return paymentCategoryRepository.findOneWithEagerRelationships(id).map(paymentCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentCategory : {}", id);
        paymentCategoryRepository.deleteById(id);
        paymentCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentCategories for query {}", query);
        return paymentCategorySearchRepository.search(query, pageable).map(paymentCategoryMapper::toDto);
    }
}
