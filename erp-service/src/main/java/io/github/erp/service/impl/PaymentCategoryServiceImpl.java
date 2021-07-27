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

import io.github.erp.service.PaymentCategoryService;
import io.github.erp.domain.PaymentCategory;
import io.github.erp.repository.PaymentCategoryRepository;
import io.github.erp.repository.search.PaymentCategorySearchRepository;
import io.github.erp.service.dto.PaymentCategoryDTO;
import io.github.erp.service.mapper.PaymentCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

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

    public PaymentCategoryServiceImpl(PaymentCategoryRepository paymentCategoryRepository, PaymentCategoryMapper paymentCategoryMapper, PaymentCategorySearchRepository paymentCategorySearchRepository) {
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
    @Transactional(readOnly = true)
    public Page<PaymentCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentCategories");
        return paymentCategoryRepository.findAll(pageable)
            .map(paymentCategoryMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentCategoryDTO> findOne(Long id) {
        log.debug("Request to get PaymentCategory : {}", id);
        return paymentCategoryRepository.findById(id)
            .map(paymentCategoryMapper::toDto);
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
        return paymentCategorySearchRepository.search(queryStringQuery(query), pageable)
            .map(paymentCategoryMapper::toDto);
    }
}
