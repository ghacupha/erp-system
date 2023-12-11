package io.github.erp.service;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.TerminalFunctions;
import io.github.erp.repository.TerminalFunctionsRepository;
import io.github.erp.repository.search.TerminalFunctionsSearchRepository;
import io.github.erp.service.criteria.TerminalFunctionsCriteria;
import io.github.erp.service.dto.TerminalFunctionsDTO;
import io.github.erp.service.mapper.TerminalFunctionsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TerminalFunctions} entities in the database.
 * The main input is a {@link TerminalFunctionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TerminalFunctionsDTO} or a {@link Page} of {@link TerminalFunctionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TerminalFunctionsQueryService extends QueryService<TerminalFunctions> {

    private final Logger log = LoggerFactory.getLogger(TerminalFunctionsQueryService.class);

    private final TerminalFunctionsRepository terminalFunctionsRepository;

    private final TerminalFunctionsMapper terminalFunctionsMapper;

    private final TerminalFunctionsSearchRepository terminalFunctionsSearchRepository;

    public TerminalFunctionsQueryService(
        TerminalFunctionsRepository terminalFunctionsRepository,
        TerminalFunctionsMapper terminalFunctionsMapper,
        TerminalFunctionsSearchRepository terminalFunctionsSearchRepository
    ) {
        this.terminalFunctionsRepository = terminalFunctionsRepository;
        this.terminalFunctionsMapper = terminalFunctionsMapper;
        this.terminalFunctionsSearchRepository = terminalFunctionsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TerminalFunctionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TerminalFunctionsDTO> findByCriteria(TerminalFunctionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TerminalFunctions> specification = createSpecification(criteria);
        return terminalFunctionsMapper.toDto(terminalFunctionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TerminalFunctionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TerminalFunctionsDTO> findByCriteria(TerminalFunctionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TerminalFunctions> specification = createSpecification(criteria);
        return terminalFunctionsRepository.findAll(specification, page).map(terminalFunctionsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TerminalFunctionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TerminalFunctions> specification = createSpecification(criteria);
        return terminalFunctionsRepository.count(specification);
    }

    /**
     * Function to convert {@link TerminalFunctionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TerminalFunctions> createSpecification(TerminalFunctionsCriteria criteria) {
        Specification<TerminalFunctions> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TerminalFunctions_.id));
            }
            if (criteria.getFunctionCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFunctionCode(), TerminalFunctions_.functionCode));
            }
            if (criteria.getTerminalFunctionality() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getTerminalFunctionality(), TerminalFunctions_.terminalFunctionality)
                    );
            }
        }
        return specification;
    }
}
