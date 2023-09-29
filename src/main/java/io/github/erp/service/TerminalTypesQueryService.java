package io.github.erp.service;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.TerminalTypes;
import io.github.erp.repository.TerminalTypesRepository;
import io.github.erp.repository.search.TerminalTypesSearchRepository;
import io.github.erp.service.criteria.TerminalTypesCriteria;
import io.github.erp.service.dto.TerminalTypesDTO;
import io.github.erp.service.mapper.TerminalTypesMapper;
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
 * Service for executing complex queries for {@link TerminalTypes} entities in the database.
 * The main input is a {@link TerminalTypesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TerminalTypesDTO} or a {@link Page} of {@link TerminalTypesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TerminalTypesQueryService extends QueryService<TerminalTypes> {

    private final Logger log = LoggerFactory.getLogger(TerminalTypesQueryService.class);

    private final TerminalTypesRepository terminalTypesRepository;

    private final TerminalTypesMapper terminalTypesMapper;

    private final TerminalTypesSearchRepository terminalTypesSearchRepository;

    public TerminalTypesQueryService(
        TerminalTypesRepository terminalTypesRepository,
        TerminalTypesMapper terminalTypesMapper,
        TerminalTypesSearchRepository terminalTypesSearchRepository
    ) {
        this.terminalTypesRepository = terminalTypesRepository;
        this.terminalTypesMapper = terminalTypesMapper;
        this.terminalTypesSearchRepository = terminalTypesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TerminalTypesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TerminalTypesDTO> findByCriteria(TerminalTypesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TerminalTypes> specification = createSpecification(criteria);
        return terminalTypesMapper.toDto(terminalTypesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TerminalTypesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TerminalTypesDTO> findByCriteria(TerminalTypesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TerminalTypes> specification = createSpecification(criteria);
        return terminalTypesRepository.findAll(specification, page).map(terminalTypesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TerminalTypesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TerminalTypes> specification = createSpecification(criteria);
        return terminalTypesRepository.count(specification);
    }

    /**
     * Function to convert {@link TerminalTypesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TerminalTypes> createSpecification(TerminalTypesCriteria criteria) {
        Specification<TerminalTypes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TerminalTypes_.id));
            }
            if (criteria.getTxnTerminalTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTxnTerminalTypeCode(), TerminalTypes_.txnTerminalTypeCode));
            }
            if (criteria.getTxnChannelType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTxnChannelType(), TerminalTypes_.txnChannelType));
            }
        }
        return specification;
    }
}
