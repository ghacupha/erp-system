package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.domain.TALeaseInterestAccrualRule;
import io.github.erp.repository.TALeaseInterestAccrualRuleRepository;
import io.github.erp.repository.search.TALeaseInterestAccrualRuleSearchRepository;
import io.github.erp.service.TALeaseInterestAccrualRuleService;
import io.github.erp.service.dto.TALeaseInterestAccrualRuleDTO;
import io.github.erp.service.mapper.TALeaseInterestAccrualRuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TALeaseInterestAccrualRule}.
 */
@Service
@Transactional
public class TALeaseInterestAccrualRuleServiceImpl implements TALeaseInterestAccrualRuleService {

    private final Logger log = LoggerFactory.getLogger(TALeaseInterestAccrualRuleServiceImpl.class);

    private final TALeaseInterestAccrualRuleRepository tALeaseInterestAccrualRuleRepository;

    private final TALeaseInterestAccrualRuleMapper tALeaseInterestAccrualRuleMapper;

    private final TALeaseInterestAccrualRuleSearchRepository tALeaseInterestAccrualRuleSearchRepository;

    public TALeaseInterestAccrualRuleServiceImpl(
        TALeaseInterestAccrualRuleRepository tALeaseInterestAccrualRuleRepository,
        TALeaseInterestAccrualRuleMapper tALeaseInterestAccrualRuleMapper,
        TALeaseInterestAccrualRuleSearchRepository tALeaseInterestAccrualRuleSearchRepository
    ) {
        this.tALeaseInterestAccrualRuleRepository = tALeaseInterestAccrualRuleRepository;
        this.tALeaseInterestAccrualRuleMapper = tALeaseInterestAccrualRuleMapper;
        this.tALeaseInterestAccrualRuleSearchRepository = tALeaseInterestAccrualRuleSearchRepository;
    }

    @Override
    public TALeaseInterestAccrualRuleDTO save(TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO) {
        log.debug("Request to save TALeaseInterestAccrualRule : {}", tALeaseInterestAccrualRuleDTO);
        TALeaseInterestAccrualRule tALeaseInterestAccrualRule = tALeaseInterestAccrualRuleMapper.toEntity(tALeaseInterestAccrualRuleDTO);
        tALeaseInterestAccrualRule = tALeaseInterestAccrualRuleRepository.save(tALeaseInterestAccrualRule);
        TALeaseInterestAccrualRuleDTO result = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);
        tALeaseInterestAccrualRuleSearchRepository.save(tALeaseInterestAccrualRule);
        return result;
    }

    @Override
    public Optional<TALeaseInterestAccrualRuleDTO> partialUpdate(TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO) {
        log.debug("Request to partially update TALeaseInterestAccrualRule : {}", tALeaseInterestAccrualRuleDTO);

        return tALeaseInterestAccrualRuleRepository
            .findById(tALeaseInterestAccrualRuleDTO.getId())
            .map(existingTALeaseInterestAccrualRule -> {
                tALeaseInterestAccrualRuleMapper.partialUpdate(existingTALeaseInterestAccrualRule, tALeaseInterestAccrualRuleDTO);

                return existingTALeaseInterestAccrualRule;
            })
            .map(tALeaseInterestAccrualRuleRepository::save)
            .map(savedTALeaseInterestAccrualRule -> {
                tALeaseInterestAccrualRuleSearchRepository.save(savedTALeaseInterestAccrualRule);

                return savedTALeaseInterestAccrualRule;
            })
            .map(tALeaseInterestAccrualRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TALeaseInterestAccrualRuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TALeaseInterestAccrualRules");
        return tALeaseInterestAccrualRuleRepository.findAll(pageable).map(tALeaseInterestAccrualRuleMapper::toDto);
    }

    public Page<TALeaseInterestAccrualRuleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tALeaseInterestAccrualRuleRepository.findAllWithEagerRelationships(pageable).map(tALeaseInterestAccrualRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TALeaseInterestAccrualRuleDTO> findOne(Long id) {
        log.debug("Request to get TALeaseInterestAccrualRule : {}", id);
        return tALeaseInterestAccrualRuleRepository.findOneWithEagerRelationships(id).map(tALeaseInterestAccrualRuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TALeaseInterestAccrualRule : {}", id);
        tALeaseInterestAccrualRuleRepository.deleteById(id);
        tALeaseInterestAccrualRuleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TALeaseInterestAccrualRuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TALeaseInterestAccrualRules for query {}", query);
        return tALeaseInterestAccrualRuleSearchRepository.search(query, pageable).map(tALeaseInterestAccrualRuleMapper::toDto);
    }
}
