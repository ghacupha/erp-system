package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.TARecognitionROURule;
import io.github.erp.repository.TARecognitionROURuleRepository;
import io.github.erp.repository.search.TARecognitionROURuleSearchRepository;
import io.github.erp.service.TARecognitionROURuleService;
import io.github.erp.service.dto.TARecognitionROURuleDTO;
import io.github.erp.service.mapper.TARecognitionROURuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TARecognitionROURule}.
 */
@Service
@Transactional
public class TARecognitionROURuleServiceImpl implements TARecognitionROURuleService {

    private final Logger log = LoggerFactory.getLogger(TARecognitionROURuleServiceImpl.class);

    private final TARecognitionROURuleRepository tARecognitionROURuleRepository;

    private final TARecognitionROURuleMapper tARecognitionROURuleMapper;

    private final TARecognitionROURuleSearchRepository tARecognitionROURuleSearchRepository;

    public TARecognitionROURuleServiceImpl(
        TARecognitionROURuleRepository tARecognitionROURuleRepository,
        TARecognitionROURuleMapper tARecognitionROURuleMapper,
        TARecognitionROURuleSearchRepository tARecognitionROURuleSearchRepository
    ) {
        this.tARecognitionROURuleRepository = tARecognitionROURuleRepository;
        this.tARecognitionROURuleMapper = tARecognitionROURuleMapper;
        this.tARecognitionROURuleSearchRepository = tARecognitionROURuleSearchRepository;
    }

    @Override
    public TARecognitionROURuleDTO save(TARecognitionROURuleDTO tARecognitionROURuleDTO) {
        log.debug("Request to save TARecognitionROURule : {}", tARecognitionROURuleDTO);
        TARecognitionROURule tARecognitionROURule = tARecognitionROURuleMapper.toEntity(tARecognitionROURuleDTO);
        tARecognitionROURule = tARecognitionROURuleRepository.save(tARecognitionROURule);
        TARecognitionROURuleDTO result = tARecognitionROURuleMapper.toDto(tARecognitionROURule);
        tARecognitionROURuleSearchRepository.save(tARecognitionROURule);
        return result;
    }

    @Override
    public Optional<TARecognitionROURuleDTO> partialUpdate(TARecognitionROURuleDTO tARecognitionROURuleDTO) {
        log.debug("Request to partially update TARecognitionROURule : {}", tARecognitionROURuleDTO);

        return tARecognitionROURuleRepository
            .findById(tARecognitionROURuleDTO.getId())
            .map(existingTARecognitionROURule -> {
                tARecognitionROURuleMapper.partialUpdate(existingTARecognitionROURule, tARecognitionROURuleDTO);

                return existingTARecognitionROURule;
            })
            .map(tARecognitionROURuleRepository::save)
            .map(savedTARecognitionROURule -> {
                tARecognitionROURuleSearchRepository.save(savedTARecognitionROURule);

                return savedTARecognitionROURule;
            })
            .map(tARecognitionROURuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TARecognitionROURuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TARecognitionROURules");
        return tARecognitionROURuleRepository.findAll(pageable).map(tARecognitionROURuleMapper::toDto);
    }

    public Page<TARecognitionROURuleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tARecognitionROURuleRepository.findAllWithEagerRelationships(pageable).map(tARecognitionROURuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TARecognitionROURuleDTO> findOne(Long id) {
        log.debug("Request to get TARecognitionROURule : {}", id);
        return tARecognitionROURuleRepository.findOneWithEagerRelationships(id).map(tARecognitionROURuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TARecognitionROURule : {}", id);
        tARecognitionROURuleRepository.deleteById(id);
        tARecognitionROURuleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TARecognitionROURuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TARecognitionROURules for query {}", query);
        return tARecognitionROURuleSearchRepository.search(query, pageable).map(tARecognitionROURuleMapper::toDto);
    }
}
