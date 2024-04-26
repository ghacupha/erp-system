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

import io.github.erp.domain.FinancialDerivativeTypeCode;
import io.github.erp.repository.FinancialDerivativeTypeCodeRepository;
import io.github.erp.repository.search.FinancialDerivativeTypeCodeSearchRepository;
import io.github.erp.service.FinancialDerivativeTypeCodeService;
import io.github.erp.service.dto.FinancialDerivativeTypeCodeDTO;
import io.github.erp.service.mapper.FinancialDerivativeTypeCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FinancialDerivativeTypeCode}.
 */
@Service
@Transactional
public class FinancialDerivativeTypeCodeServiceImpl implements FinancialDerivativeTypeCodeService {

    private final Logger log = LoggerFactory.getLogger(FinancialDerivativeTypeCodeServiceImpl.class);

    private final FinancialDerivativeTypeCodeRepository financialDerivativeTypeCodeRepository;

    private final FinancialDerivativeTypeCodeMapper financialDerivativeTypeCodeMapper;

    private final FinancialDerivativeTypeCodeSearchRepository financialDerivativeTypeCodeSearchRepository;

    public FinancialDerivativeTypeCodeServiceImpl(
        FinancialDerivativeTypeCodeRepository financialDerivativeTypeCodeRepository,
        FinancialDerivativeTypeCodeMapper financialDerivativeTypeCodeMapper,
        FinancialDerivativeTypeCodeSearchRepository financialDerivativeTypeCodeSearchRepository
    ) {
        this.financialDerivativeTypeCodeRepository = financialDerivativeTypeCodeRepository;
        this.financialDerivativeTypeCodeMapper = financialDerivativeTypeCodeMapper;
        this.financialDerivativeTypeCodeSearchRepository = financialDerivativeTypeCodeSearchRepository;
    }

    @Override
    public FinancialDerivativeTypeCodeDTO save(FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO) {
        log.debug("Request to save FinancialDerivativeTypeCode : {}", financialDerivativeTypeCodeDTO);
        FinancialDerivativeTypeCode financialDerivativeTypeCode = financialDerivativeTypeCodeMapper.toEntity(
            financialDerivativeTypeCodeDTO
        );
        financialDerivativeTypeCode = financialDerivativeTypeCodeRepository.save(financialDerivativeTypeCode);
        FinancialDerivativeTypeCodeDTO result = financialDerivativeTypeCodeMapper.toDto(financialDerivativeTypeCode);
        financialDerivativeTypeCodeSearchRepository.save(financialDerivativeTypeCode);
        return result;
    }

    @Override
    public Optional<FinancialDerivativeTypeCodeDTO> partialUpdate(FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO) {
        log.debug("Request to partially update FinancialDerivativeTypeCode : {}", financialDerivativeTypeCodeDTO);

        return financialDerivativeTypeCodeRepository
            .findById(financialDerivativeTypeCodeDTO.getId())
            .map(existingFinancialDerivativeTypeCode -> {
                financialDerivativeTypeCodeMapper.partialUpdate(existingFinancialDerivativeTypeCode, financialDerivativeTypeCodeDTO);

                return existingFinancialDerivativeTypeCode;
            })
            .map(financialDerivativeTypeCodeRepository::save)
            .map(savedFinancialDerivativeTypeCode -> {
                financialDerivativeTypeCodeSearchRepository.save(savedFinancialDerivativeTypeCode);

                return savedFinancialDerivativeTypeCode;
            })
            .map(financialDerivativeTypeCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FinancialDerivativeTypeCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FinancialDerivativeTypeCodes");
        return financialDerivativeTypeCodeRepository.findAll(pageable).map(financialDerivativeTypeCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FinancialDerivativeTypeCodeDTO> findOne(Long id) {
        log.debug("Request to get FinancialDerivativeTypeCode : {}", id);
        return financialDerivativeTypeCodeRepository.findById(id).map(financialDerivativeTypeCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FinancialDerivativeTypeCode : {}", id);
        financialDerivativeTypeCodeRepository.deleteById(id);
        financialDerivativeTypeCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FinancialDerivativeTypeCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FinancialDerivativeTypeCodes for query {}", query);
        return financialDerivativeTypeCodeSearchRepository.search(query, pageable).map(financialDerivativeTypeCodeMapper::toDto);
    }
}
