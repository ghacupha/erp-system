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
import io.github.erp.domain.TAInterestPaidTransferRule;
import io.github.erp.repository.TAInterestPaidTransferRuleRepository;
import io.github.erp.repository.search.TAInterestPaidTransferRuleSearchRepository;
import io.github.erp.service.TAInterestPaidTransferRuleService;
import io.github.erp.service.dto.TAInterestPaidTransferRuleDTO;
import io.github.erp.service.mapper.TAInterestPaidTransferRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TAInterestPaidTransferRule}.
 */
@Service
@Transactional
public class InternalTAInterestPaidTransferRuleServiceImpl implements InternalTAInterestPaidTransferRuleService {

    private final Logger log = LoggerFactory.getLogger(InternalTAInterestPaidTransferRuleServiceImpl.class);

    private final TAInterestPaidTransferRuleRepository tAInterestPaidTransferRuleRepository;

    private final TAInterestPaidTransferRuleMapper tAInterestPaidTransferRuleMapper;

    private final TAInterestPaidTransferRuleSearchRepository tAInterestPaidTransferRuleSearchRepository;

    public InternalTAInterestPaidTransferRuleServiceImpl(
        TAInterestPaidTransferRuleRepository tAInterestPaidTransferRuleRepository,
        TAInterestPaidTransferRuleMapper tAInterestPaidTransferRuleMapper,
        TAInterestPaidTransferRuleSearchRepository tAInterestPaidTransferRuleSearchRepository
    ) {
        this.tAInterestPaidTransferRuleRepository = tAInterestPaidTransferRuleRepository;
        this.tAInterestPaidTransferRuleMapper = tAInterestPaidTransferRuleMapper;
        this.tAInterestPaidTransferRuleSearchRepository = tAInterestPaidTransferRuleSearchRepository;
    }

    @Override
    public TAInterestPaidTransferRuleDTO save(TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO) {
        log.debug("Request to save TAInterestPaidTransferRule : {}", tAInterestPaidTransferRuleDTO);
        TAInterestPaidTransferRule tAInterestPaidTransferRule = tAInterestPaidTransferRuleMapper.toEntity(tAInterestPaidTransferRuleDTO);
        tAInterestPaidTransferRule = tAInterestPaidTransferRuleRepository.save(tAInterestPaidTransferRule);
        TAInterestPaidTransferRuleDTO result = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);
        tAInterestPaidTransferRuleSearchRepository.save(tAInterestPaidTransferRule);
        return result;
    }

    @Override
    public Optional<TAInterestPaidTransferRuleDTO> partialUpdate(TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO) {
        log.debug("Request to partially update TAInterestPaidTransferRule : {}", tAInterestPaidTransferRuleDTO);

        return tAInterestPaidTransferRuleRepository
            .findById(tAInterestPaidTransferRuleDTO.getId())
            .map(existingTAInterestPaidTransferRule -> {
                tAInterestPaidTransferRuleMapper.partialUpdate(existingTAInterestPaidTransferRule, tAInterestPaidTransferRuleDTO);

                return existingTAInterestPaidTransferRule;
            })
            .map(tAInterestPaidTransferRuleRepository::save)
            .map(savedTAInterestPaidTransferRule -> {
                tAInterestPaidTransferRuleSearchRepository.save(savedTAInterestPaidTransferRule);

                return savedTAInterestPaidTransferRule;
            })
            .map(tAInterestPaidTransferRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TAInterestPaidTransferRuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TAInterestPaidTransferRules");
        return tAInterestPaidTransferRuleRepository.findAll(pageable).map(tAInterestPaidTransferRuleMapper::toDto);
    }

    public Page<TAInterestPaidTransferRuleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tAInterestPaidTransferRuleRepository.findAllWithEagerRelationships(pageable).map(tAInterestPaidTransferRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TAInterestPaidTransferRuleDTO> findOne(Long id) {
        log.debug("Request to get TAInterestPaidTransferRule : {}", id);
        return tAInterestPaidTransferRuleRepository.findOneWithEagerRelationships(id).map(tAInterestPaidTransferRuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TAInterestPaidTransferRule : {}", id);
        tAInterestPaidTransferRuleRepository.deleteById(id);
        tAInterestPaidTransferRuleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TAInterestPaidTransferRuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TAInterestPaidTransferRules for query {}", query);
        return tAInterestPaidTransferRuleSearchRepository.search(query, pageable).map(tAInterestPaidTransferRuleMapper::toDto);
    }
}
