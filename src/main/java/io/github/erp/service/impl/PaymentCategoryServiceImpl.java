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
