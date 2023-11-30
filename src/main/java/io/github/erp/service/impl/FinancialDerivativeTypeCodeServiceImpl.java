package io.github.erp.service.impl;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
