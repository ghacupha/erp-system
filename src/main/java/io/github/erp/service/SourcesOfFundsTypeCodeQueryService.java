package io.github.erp.service;

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

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.SourcesOfFundsTypeCode;
import io.github.erp.repository.SourcesOfFundsTypeCodeRepository;
import io.github.erp.repository.search.SourcesOfFundsTypeCodeSearchRepository;
import io.github.erp.service.criteria.SourcesOfFundsTypeCodeCriteria;
import io.github.erp.service.dto.SourcesOfFundsTypeCodeDTO;
import io.github.erp.service.mapper.SourcesOfFundsTypeCodeMapper;
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
 * Service for executing complex queries for {@link SourcesOfFundsTypeCode} entities in the database.
 * The main input is a {@link SourcesOfFundsTypeCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SourcesOfFundsTypeCodeDTO} or a {@link Page} of {@link SourcesOfFundsTypeCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SourcesOfFundsTypeCodeQueryService extends QueryService<SourcesOfFundsTypeCode> {

    private final Logger log = LoggerFactory.getLogger(SourcesOfFundsTypeCodeQueryService.class);

    private final SourcesOfFundsTypeCodeRepository sourcesOfFundsTypeCodeRepository;

    private final SourcesOfFundsTypeCodeMapper sourcesOfFundsTypeCodeMapper;

    private final SourcesOfFundsTypeCodeSearchRepository sourcesOfFundsTypeCodeSearchRepository;

    public SourcesOfFundsTypeCodeQueryService(
        SourcesOfFundsTypeCodeRepository sourcesOfFundsTypeCodeRepository,
        SourcesOfFundsTypeCodeMapper sourcesOfFundsTypeCodeMapper,
        SourcesOfFundsTypeCodeSearchRepository sourcesOfFundsTypeCodeSearchRepository
    ) {
        this.sourcesOfFundsTypeCodeRepository = sourcesOfFundsTypeCodeRepository;
        this.sourcesOfFundsTypeCodeMapper = sourcesOfFundsTypeCodeMapper;
        this.sourcesOfFundsTypeCodeSearchRepository = sourcesOfFundsTypeCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SourcesOfFundsTypeCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SourcesOfFundsTypeCodeDTO> findByCriteria(SourcesOfFundsTypeCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SourcesOfFundsTypeCode> specification = createSpecification(criteria);
        return sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SourcesOfFundsTypeCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SourcesOfFundsTypeCodeDTO> findByCriteria(SourcesOfFundsTypeCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SourcesOfFundsTypeCode> specification = createSpecification(criteria);
        return sourcesOfFundsTypeCodeRepository.findAll(specification, page).map(sourcesOfFundsTypeCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SourcesOfFundsTypeCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SourcesOfFundsTypeCode> specification = createSpecification(criteria);
        return sourcesOfFundsTypeCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link SourcesOfFundsTypeCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SourcesOfFundsTypeCode> createSpecification(SourcesOfFundsTypeCodeCriteria criteria) {
        Specification<SourcesOfFundsTypeCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SourcesOfFundsTypeCode_.id));
            }
            if (criteria.getSourceOfFundsTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getSourceOfFundsTypeCode(), SourcesOfFundsTypeCode_.sourceOfFundsTypeCode)
                    );
            }
            if (criteria.getSourceOfFundsType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSourceOfFundsType(), SourcesOfFundsTypeCode_.sourceOfFundsType));
            }
        }
        return specification;
    }
}
