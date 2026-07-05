package io.github.erp.internal.service.ledgers;

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
import io.github.erp.domain.TALeaseRepaymentRule;
import io.github.erp.repository.TALeaseRepaymentRuleRepository;
import io.github.erp.repository.search.TALeaseRepaymentRuleSearchRepository;
import io.github.erp.service.TALeaseRepaymentRuleService;
import io.github.erp.service.dto.TALeaseRepaymentRuleDTO;
import io.github.erp.service.mapper.TALeaseRepaymentRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TALeaseRepaymentRule}.
 */
@Service
@Transactional
public class InternalTALeaseRepaymentRuleServiceImpl implements InternalTALeaseRepaymentRuleService {

    private final Logger log = LoggerFactory.getLogger(InternalTALeaseRepaymentRuleServiceImpl.class);

    private final TALeaseRepaymentRuleRepository tALeaseRepaymentRuleRepository;

    private final TALeaseRepaymentRuleMapper tALeaseRepaymentRuleMapper;

    private final TALeaseRepaymentRuleSearchRepository tALeaseRepaymentRuleSearchRepository;

    public InternalTALeaseRepaymentRuleServiceImpl(
        TALeaseRepaymentRuleRepository tALeaseRepaymentRuleRepository,
        TALeaseRepaymentRuleMapper tALeaseRepaymentRuleMapper,
        TALeaseRepaymentRuleSearchRepository tALeaseRepaymentRuleSearchRepository
    ) {
        this.tALeaseRepaymentRuleRepository = tALeaseRepaymentRuleRepository;
        this.tALeaseRepaymentRuleMapper = tALeaseRepaymentRuleMapper;
        this.tALeaseRepaymentRuleSearchRepository = tALeaseRepaymentRuleSearchRepository;
    }

    @Override
    public TALeaseRepaymentRuleDTO save(TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO) {
        log.debug("Request to save TALeaseRepaymentRule : {}", tALeaseRepaymentRuleDTO);
        TALeaseRepaymentRule tALeaseRepaymentRule = tALeaseRepaymentRuleMapper.toEntity(tALeaseRepaymentRuleDTO);
        tALeaseRepaymentRule = tALeaseRepaymentRuleRepository.save(tALeaseRepaymentRule);
        TALeaseRepaymentRuleDTO result = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);
        tALeaseRepaymentRuleSearchRepository.save(tALeaseRepaymentRule);
        return result;
    }

    @Override
    public Optional<TALeaseRepaymentRuleDTO> partialUpdate(TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO) {
        log.debug("Request to partially update TALeaseRepaymentRule : {}", tALeaseRepaymentRuleDTO);

        return tALeaseRepaymentRuleRepository
            .findById(tALeaseRepaymentRuleDTO.getId())
            .map(existingTALeaseRepaymentRule -> {
                tALeaseRepaymentRuleMapper.partialUpdate(existingTALeaseRepaymentRule, tALeaseRepaymentRuleDTO);

                return existingTALeaseRepaymentRule;
            })
            .map(tALeaseRepaymentRuleRepository::save)
            .map(savedTALeaseRepaymentRule -> {
                tALeaseRepaymentRuleSearchRepository.save(savedTALeaseRepaymentRule);

                return savedTALeaseRepaymentRule;
            })
            .map(tALeaseRepaymentRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TALeaseRepaymentRuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TALeaseRepaymentRules");
        return tALeaseRepaymentRuleRepository.findAll(pageable).map(tALeaseRepaymentRuleMapper::toDto);
    }

    public Page<TALeaseRepaymentRuleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tALeaseRepaymentRuleRepository.findAllWithEagerRelationships(pageable).map(tALeaseRepaymentRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TALeaseRepaymentRuleDTO> findOne(Long id) {
        log.debug("Request to get TALeaseRepaymentRule : {}", id);
        return tALeaseRepaymentRuleRepository.findOneWithEagerRelationships(id).map(tALeaseRepaymentRuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TALeaseRepaymentRule : {}", id);
        tALeaseRepaymentRuleRepository.deleteById(id);
        tALeaseRepaymentRuleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TALeaseRepaymentRuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TALeaseRepaymentRules for query {}", query);
        return tALeaseRepaymentRuleSearchRepository.search(query, pageable).map(tALeaseRepaymentRuleMapper::toDto);
    }
}
