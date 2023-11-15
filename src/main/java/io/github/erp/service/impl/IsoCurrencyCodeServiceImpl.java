package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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

import io.github.erp.domain.IsoCurrencyCode;
import io.github.erp.repository.IsoCurrencyCodeRepository;
import io.github.erp.repository.search.IsoCurrencyCodeSearchRepository;
import io.github.erp.service.IsoCurrencyCodeService;
import io.github.erp.service.dto.IsoCurrencyCodeDTO;
import io.github.erp.service.mapper.IsoCurrencyCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IsoCurrencyCode}.
 */
@Service
@Transactional
public class IsoCurrencyCodeServiceImpl implements IsoCurrencyCodeService {

    private final Logger log = LoggerFactory.getLogger(IsoCurrencyCodeServiceImpl.class);

    private final IsoCurrencyCodeRepository isoCurrencyCodeRepository;

    private final IsoCurrencyCodeMapper isoCurrencyCodeMapper;

    private final IsoCurrencyCodeSearchRepository isoCurrencyCodeSearchRepository;

    public IsoCurrencyCodeServiceImpl(
        IsoCurrencyCodeRepository isoCurrencyCodeRepository,
        IsoCurrencyCodeMapper isoCurrencyCodeMapper,
        IsoCurrencyCodeSearchRepository isoCurrencyCodeSearchRepository
    ) {
        this.isoCurrencyCodeRepository = isoCurrencyCodeRepository;
        this.isoCurrencyCodeMapper = isoCurrencyCodeMapper;
        this.isoCurrencyCodeSearchRepository = isoCurrencyCodeSearchRepository;
    }

    @Override
    public IsoCurrencyCodeDTO save(IsoCurrencyCodeDTO isoCurrencyCodeDTO) {
        log.debug("Request to save IsoCurrencyCode : {}", isoCurrencyCodeDTO);
        IsoCurrencyCode isoCurrencyCode = isoCurrencyCodeMapper.toEntity(isoCurrencyCodeDTO);
        isoCurrencyCode = isoCurrencyCodeRepository.save(isoCurrencyCode);
        IsoCurrencyCodeDTO result = isoCurrencyCodeMapper.toDto(isoCurrencyCode);
        isoCurrencyCodeSearchRepository.save(isoCurrencyCode);
        return result;
    }

    @Override
    public Optional<IsoCurrencyCodeDTO> partialUpdate(IsoCurrencyCodeDTO isoCurrencyCodeDTO) {
        log.debug("Request to partially update IsoCurrencyCode : {}", isoCurrencyCodeDTO);

        return isoCurrencyCodeRepository
            .findById(isoCurrencyCodeDTO.getId())
            .map(existingIsoCurrencyCode -> {
                isoCurrencyCodeMapper.partialUpdate(existingIsoCurrencyCode, isoCurrencyCodeDTO);

                return existingIsoCurrencyCode;
            })
            .map(isoCurrencyCodeRepository::save)
            .map(savedIsoCurrencyCode -> {
                isoCurrencyCodeSearchRepository.save(savedIsoCurrencyCode);

                return savedIsoCurrencyCode;
            })
            .map(isoCurrencyCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsoCurrencyCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IsoCurrencyCodes");
        return isoCurrencyCodeRepository.findAll(pageable).map(isoCurrencyCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IsoCurrencyCodeDTO> findOne(Long id) {
        log.debug("Request to get IsoCurrencyCode : {}", id);
        return isoCurrencyCodeRepository.findById(id).map(isoCurrencyCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IsoCurrencyCode : {}", id);
        isoCurrencyCodeRepository.deleteById(id);
        isoCurrencyCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsoCurrencyCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IsoCurrencyCodes for query {}", query);
        return isoCurrencyCodeSearchRepository.search(query, pageable).map(isoCurrencyCodeMapper::toDto);
    }
}
