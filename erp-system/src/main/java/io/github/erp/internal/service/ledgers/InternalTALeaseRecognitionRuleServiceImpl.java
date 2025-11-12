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
import io.github.erp.domain.TALeaseRecognitionRule;
import io.github.erp.repository.TALeaseRecognitionRuleRepository;
import io.github.erp.repository.search.TALeaseRecognitionRuleSearchRepository;
import io.github.erp.service.TALeaseRecognitionRuleService;
import io.github.erp.service.dto.TALeaseRecognitionRuleDTO;
import io.github.erp.service.mapper.TALeaseRecognitionRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TALeaseRecognitionRule}.
 */
@Service
@Transactional
public class InternalTALeaseRecognitionRuleServiceImpl implements InternalTALeaseRecognitionRuleService {

    private final Logger log = LoggerFactory.getLogger(InternalTALeaseRecognitionRuleServiceImpl.class);

    private final TALeaseRecognitionRuleRepository tALeaseRecognitionRuleRepository;

    private final TALeaseRecognitionRuleMapper tALeaseRecognitionRuleMapper;

    private final TALeaseRecognitionRuleSearchRepository tALeaseRecognitionRuleSearchRepository;

    public InternalTALeaseRecognitionRuleServiceImpl(
        TALeaseRecognitionRuleRepository tALeaseRecognitionRuleRepository,
        TALeaseRecognitionRuleMapper tALeaseRecognitionRuleMapper,
        TALeaseRecognitionRuleSearchRepository tALeaseRecognitionRuleSearchRepository
    ) {
        this.tALeaseRecognitionRuleRepository = tALeaseRecognitionRuleRepository;
        this.tALeaseRecognitionRuleMapper = tALeaseRecognitionRuleMapper;
        this.tALeaseRecognitionRuleSearchRepository = tALeaseRecognitionRuleSearchRepository;
    }

    @Override
    public TALeaseRecognitionRuleDTO save(TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO) {
        log.debug("Request to save TALeaseRecognitionRule : {}", tALeaseRecognitionRuleDTO);
        TALeaseRecognitionRule tALeaseRecognitionRule = tALeaseRecognitionRuleMapper.toEntity(tALeaseRecognitionRuleDTO);
        tALeaseRecognitionRule = tALeaseRecognitionRuleRepository.save(tALeaseRecognitionRule);
        TALeaseRecognitionRuleDTO result = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);
        tALeaseRecognitionRuleSearchRepository.save(tALeaseRecognitionRule);
        return result;
    }

    @Override
    public Optional<TALeaseRecognitionRuleDTO> partialUpdate(TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO) {
        log.debug("Request to partially update TALeaseRecognitionRule : {}", tALeaseRecognitionRuleDTO);

        return tALeaseRecognitionRuleRepository
            .findById(tALeaseRecognitionRuleDTO.getId())
            .map(existingTALeaseRecognitionRule -> {
                tALeaseRecognitionRuleMapper.partialUpdate(existingTALeaseRecognitionRule, tALeaseRecognitionRuleDTO);

                return existingTALeaseRecognitionRule;
            })
            .map(tALeaseRecognitionRuleRepository::save)
            .map(savedTALeaseRecognitionRule -> {
                tALeaseRecognitionRuleSearchRepository.save(savedTALeaseRecognitionRule);

                return savedTALeaseRecognitionRule;
            })
            .map(tALeaseRecognitionRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TALeaseRecognitionRuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TALeaseRecognitionRules");
        return tALeaseRecognitionRuleRepository.findAll(pageable).map(tALeaseRecognitionRuleMapper::toDto);
    }

    public Page<TALeaseRecognitionRuleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tALeaseRecognitionRuleRepository.findAllWithEagerRelationships(pageable).map(tALeaseRecognitionRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TALeaseRecognitionRuleDTO> findOne(Long id) {
        log.debug("Request to get TALeaseRecognitionRule : {}", id);
        return tALeaseRecognitionRuleRepository.findOneWithEagerRelationships(id).map(tALeaseRecognitionRuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TALeaseRecognitionRule : {}", id);
        tALeaseRecognitionRuleRepository.deleteById(id);
        tALeaseRecognitionRuleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TALeaseRecognitionRuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TALeaseRecognitionRules for query {}", query);
        return tALeaseRecognitionRuleSearchRepository.search(query, pageable).map(tALeaseRecognitionRuleMapper::toDto);
    }
}
