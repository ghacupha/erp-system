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
import io.github.erp.domain.LegalStatus;
import io.github.erp.repository.LegalStatusRepository;
import io.github.erp.repository.search.LegalStatusSearchRepository;
import io.github.erp.service.criteria.LegalStatusCriteria;
import io.github.erp.service.dto.LegalStatusDTO;
import io.github.erp.service.mapper.LegalStatusMapper;
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
 * Service for executing complex queries for {@link LegalStatus} entities in the database.
 * The main input is a {@link LegalStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LegalStatusDTO} or a {@link Page} of {@link LegalStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LegalStatusQueryService extends QueryService<LegalStatus> {

    private final Logger log = LoggerFactory.getLogger(LegalStatusQueryService.class);

    private final LegalStatusRepository legalStatusRepository;

    private final LegalStatusMapper legalStatusMapper;

    private final LegalStatusSearchRepository legalStatusSearchRepository;

    public LegalStatusQueryService(
        LegalStatusRepository legalStatusRepository,
        LegalStatusMapper legalStatusMapper,
        LegalStatusSearchRepository legalStatusSearchRepository
    ) {
        this.legalStatusRepository = legalStatusRepository;
        this.legalStatusMapper = legalStatusMapper;
        this.legalStatusSearchRepository = legalStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LegalStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LegalStatusDTO> findByCriteria(LegalStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LegalStatus> specification = createSpecification(criteria);
        return legalStatusMapper.toDto(legalStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LegalStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LegalStatusDTO> findByCriteria(LegalStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LegalStatus> specification = createSpecification(criteria);
        return legalStatusRepository.findAll(specification, page).map(legalStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LegalStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LegalStatus> specification = createSpecification(criteria);
        return legalStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link LegalStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LegalStatus> createSpecification(LegalStatusCriteria criteria) {
        Specification<LegalStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LegalStatus_.id));
            }
            if (criteria.getLegalStatusCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLegalStatusCode(), LegalStatus_.legalStatusCode));
            }
            if (criteria.getLegalStatusType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLegalStatusType(), LegalStatus_.legalStatusType));
            }
        }
        return specification;
    }
}
