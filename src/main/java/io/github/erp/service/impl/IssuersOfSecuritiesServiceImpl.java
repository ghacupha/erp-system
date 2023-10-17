package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

import io.github.erp.domain.IssuersOfSecurities;
import io.github.erp.repository.IssuersOfSecuritiesRepository;
import io.github.erp.repository.search.IssuersOfSecuritiesSearchRepository;
import io.github.erp.service.IssuersOfSecuritiesService;
import io.github.erp.service.dto.IssuersOfSecuritiesDTO;
import io.github.erp.service.mapper.IssuersOfSecuritiesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IssuersOfSecurities}.
 */
@Service
@Transactional
public class IssuersOfSecuritiesServiceImpl implements IssuersOfSecuritiesService {

    private final Logger log = LoggerFactory.getLogger(IssuersOfSecuritiesServiceImpl.class);

    private final IssuersOfSecuritiesRepository issuersOfSecuritiesRepository;

    private final IssuersOfSecuritiesMapper issuersOfSecuritiesMapper;

    private final IssuersOfSecuritiesSearchRepository issuersOfSecuritiesSearchRepository;

    public IssuersOfSecuritiesServiceImpl(
        IssuersOfSecuritiesRepository issuersOfSecuritiesRepository,
        IssuersOfSecuritiesMapper issuersOfSecuritiesMapper,
        IssuersOfSecuritiesSearchRepository issuersOfSecuritiesSearchRepository
    ) {
        this.issuersOfSecuritiesRepository = issuersOfSecuritiesRepository;
        this.issuersOfSecuritiesMapper = issuersOfSecuritiesMapper;
        this.issuersOfSecuritiesSearchRepository = issuersOfSecuritiesSearchRepository;
    }

    @Override
    public IssuersOfSecuritiesDTO save(IssuersOfSecuritiesDTO issuersOfSecuritiesDTO) {
        log.debug("Request to save IssuersOfSecurities : {}", issuersOfSecuritiesDTO);
        IssuersOfSecurities issuersOfSecurities = issuersOfSecuritiesMapper.toEntity(issuersOfSecuritiesDTO);
        issuersOfSecurities = issuersOfSecuritiesRepository.save(issuersOfSecurities);
        IssuersOfSecuritiesDTO result = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);
        issuersOfSecuritiesSearchRepository.save(issuersOfSecurities);
        return result;
    }

    @Override
    public Optional<IssuersOfSecuritiesDTO> partialUpdate(IssuersOfSecuritiesDTO issuersOfSecuritiesDTO) {
        log.debug("Request to partially update IssuersOfSecurities : {}", issuersOfSecuritiesDTO);

        return issuersOfSecuritiesRepository
            .findById(issuersOfSecuritiesDTO.getId())
            .map(existingIssuersOfSecurities -> {
                issuersOfSecuritiesMapper.partialUpdate(existingIssuersOfSecurities, issuersOfSecuritiesDTO);

                return existingIssuersOfSecurities;
            })
            .map(issuersOfSecuritiesRepository::save)
            .map(savedIssuersOfSecurities -> {
                issuersOfSecuritiesSearchRepository.save(savedIssuersOfSecurities);

                return savedIssuersOfSecurities;
            })
            .map(issuersOfSecuritiesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IssuersOfSecuritiesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IssuersOfSecurities");
        return issuersOfSecuritiesRepository.findAll(pageable).map(issuersOfSecuritiesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IssuersOfSecuritiesDTO> findOne(Long id) {
        log.debug("Request to get IssuersOfSecurities : {}", id);
        return issuersOfSecuritiesRepository.findById(id).map(issuersOfSecuritiesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IssuersOfSecurities : {}", id);
        issuersOfSecuritiesRepository.deleteById(id);
        issuersOfSecuritiesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IssuersOfSecuritiesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IssuersOfSecurities for query {}", query);
        return issuersOfSecuritiesSearchRepository.search(query, pageable).map(issuersOfSecuritiesMapper::toDto);
    }
}
