package io.github.erp.service;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.ProcessStatus;
import io.github.erp.repository.ProcessStatusRepository;
import io.github.erp.repository.search.ProcessStatusSearchRepository;
import io.github.erp.service.criteria.ProcessStatusCriteria;
import io.github.erp.service.dto.ProcessStatusDTO;
import io.github.erp.service.mapper.ProcessStatusMapper;
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
 * Service for executing complex queries for {@link ProcessStatus} entities in the database.
 * The main input is a {@link ProcessStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProcessStatusDTO} or a {@link Page} of {@link ProcessStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProcessStatusQueryService extends QueryService<ProcessStatus> {

    private final Logger log = LoggerFactory.getLogger(ProcessStatusQueryService.class);

    private final ProcessStatusRepository processStatusRepository;

    private final ProcessStatusMapper processStatusMapper;

    private final ProcessStatusSearchRepository processStatusSearchRepository;

    public ProcessStatusQueryService(
        ProcessStatusRepository processStatusRepository,
        ProcessStatusMapper processStatusMapper,
        ProcessStatusSearchRepository processStatusSearchRepository
    ) {
        this.processStatusRepository = processStatusRepository;
        this.processStatusMapper = processStatusMapper;
        this.processStatusSearchRepository = processStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProcessStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProcessStatusDTO> findByCriteria(ProcessStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProcessStatus> specification = createSpecification(criteria);
        return processStatusMapper.toDto(processStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProcessStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProcessStatusDTO> findByCriteria(ProcessStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProcessStatus> specification = createSpecification(criteria);
        return processStatusRepository.findAll(specification, page).map(processStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProcessStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProcessStatus> specification = createSpecification(criteria);
        return processStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link ProcessStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProcessStatus> createSpecification(ProcessStatusCriteria criteria) {
        Specification<ProcessStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProcessStatus_.id));
            }
            if (criteria.getStatusCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatusCode(), ProcessStatus_.statusCode));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProcessStatus_.description));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(ProcessStatus_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParametersId(),
                            root -> root.join(ProcessStatus_.parameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
