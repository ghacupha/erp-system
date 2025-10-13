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

import io.github.erp.domain.TAAmortizationRule;
import io.github.erp.repository.TAAmortizationRuleRepository;
import io.github.erp.repository.search.TAAmortizationRuleSearchRepository;
import io.github.erp.service.TAAmortizationRuleService;
import io.github.erp.service.dto.TAAmortizationRuleDTO;
import io.github.erp.service.mapper.TAAmortizationRuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TAAmortizationRule}.
 */
@Service
@Transactional
public class TAAmortizationRuleServiceImpl implements TAAmortizationRuleService {

    private final Logger log = LoggerFactory.getLogger(TAAmortizationRuleServiceImpl.class);

    private final TAAmortizationRuleRepository tAAmortizationRuleRepository;

    private final TAAmortizationRuleMapper tAAmortizationRuleMapper;

    private final TAAmortizationRuleSearchRepository tAAmortizationRuleSearchRepository;

    public TAAmortizationRuleServiceImpl(
        TAAmortizationRuleRepository tAAmortizationRuleRepository,
        TAAmortizationRuleMapper tAAmortizationRuleMapper,
        TAAmortizationRuleSearchRepository tAAmortizationRuleSearchRepository
    ) {
        this.tAAmortizationRuleRepository = tAAmortizationRuleRepository;
        this.tAAmortizationRuleMapper = tAAmortizationRuleMapper;
        this.tAAmortizationRuleSearchRepository = tAAmortizationRuleSearchRepository;
    }

    @Override
    public TAAmortizationRuleDTO save(TAAmortizationRuleDTO tAAmortizationRuleDTO) {
        log.debug("Request to save TAAmortizationRule : {}", tAAmortizationRuleDTO);
        TAAmortizationRule tAAmortizationRule = tAAmortizationRuleMapper.toEntity(tAAmortizationRuleDTO);
        tAAmortizationRule = tAAmortizationRuleRepository.save(tAAmortizationRule);
        TAAmortizationRuleDTO result = tAAmortizationRuleMapper.toDto(tAAmortizationRule);
        tAAmortizationRuleSearchRepository.save(tAAmortizationRule);
        return result;
    }

    @Override
    public Optional<TAAmortizationRuleDTO> partialUpdate(TAAmortizationRuleDTO tAAmortizationRuleDTO) {
        log.debug("Request to partially update TAAmortizationRule : {}", tAAmortizationRuleDTO);

        return tAAmortizationRuleRepository
            .findById(tAAmortizationRuleDTO.getId())
            .map(existingTAAmortizationRule -> {
                tAAmortizationRuleMapper.partialUpdate(existingTAAmortizationRule, tAAmortizationRuleDTO);

                return existingTAAmortizationRule;
            })
            .map(tAAmortizationRuleRepository::save)
            .map(savedTAAmortizationRule -> {
                tAAmortizationRuleSearchRepository.save(savedTAAmortizationRule);

                return savedTAAmortizationRule;
            })
            .map(tAAmortizationRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TAAmortizationRuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TAAmortizationRules");
        return tAAmortizationRuleRepository.findAll(pageable).map(tAAmortizationRuleMapper::toDto);
    }

    public Page<TAAmortizationRuleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tAAmortizationRuleRepository.findAllWithEagerRelationships(pageable).map(tAAmortizationRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TAAmortizationRuleDTO> findOne(Long id) {
        log.debug("Request to get TAAmortizationRule : {}", id);
        return tAAmortizationRuleRepository.findOneWithEagerRelationships(id).map(tAAmortizationRuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TAAmortizationRule : {}", id);
        tAAmortizationRuleRepository.deleteById(id);
        tAAmortizationRuleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TAAmortizationRuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TAAmortizationRules for query {}", query);
        return tAAmortizationRuleSearchRepository.search(query, pageable).map(tAAmortizationRuleMapper::toDto);
    }
}
