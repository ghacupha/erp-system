package io.github.erp.service;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.CrbFileTransmissionStatus;
import io.github.erp.repository.CrbFileTransmissionStatusRepository;
import io.github.erp.repository.search.CrbFileTransmissionStatusSearchRepository;
import io.github.erp.service.criteria.CrbFileTransmissionStatusCriteria;
import io.github.erp.service.dto.CrbFileTransmissionStatusDTO;
import io.github.erp.service.mapper.CrbFileTransmissionStatusMapper;
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
 * Service for executing complex queries for {@link CrbFileTransmissionStatus} entities in the database.
 * The main input is a {@link CrbFileTransmissionStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbFileTransmissionStatusDTO} or a {@link Page} of {@link CrbFileTransmissionStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbFileTransmissionStatusQueryService extends QueryService<CrbFileTransmissionStatus> {

    private final Logger log = LoggerFactory.getLogger(CrbFileTransmissionStatusQueryService.class);

    private final CrbFileTransmissionStatusRepository crbFileTransmissionStatusRepository;

    private final CrbFileTransmissionStatusMapper crbFileTransmissionStatusMapper;

    private final CrbFileTransmissionStatusSearchRepository crbFileTransmissionStatusSearchRepository;

    public CrbFileTransmissionStatusQueryService(
        CrbFileTransmissionStatusRepository crbFileTransmissionStatusRepository,
        CrbFileTransmissionStatusMapper crbFileTransmissionStatusMapper,
        CrbFileTransmissionStatusSearchRepository crbFileTransmissionStatusSearchRepository
    ) {
        this.crbFileTransmissionStatusRepository = crbFileTransmissionStatusRepository;
        this.crbFileTransmissionStatusMapper = crbFileTransmissionStatusMapper;
        this.crbFileTransmissionStatusSearchRepository = crbFileTransmissionStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbFileTransmissionStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbFileTransmissionStatusDTO> findByCriteria(CrbFileTransmissionStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbFileTransmissionStatus> specification = createSpecification(criteria);
        return crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbFileTransmissionStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbFileTransmissionStatusDTO> findByCriteria(CrbFileTransmissionStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbFileTransmissionStatus> specification = createSpecification(criteria);
        return crbFileTransmissionStatusRepository.findAll(specification, page).map(crbFileTransmissionStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbFileTransmissionStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbFileTransmissionStatus> specification = createSpecification(criteria);
        return crbFileTransmissionStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbFileTransmissionStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbFileTransmissionStatus> createSpecification(CrbFileTransmissionStatusCriteria criteria) {
        Specification<CrbFileTransmissionStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbFileTransmissionStatus_.id));
            }
            if (criteria.getSubmittedFileStatusTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSubmittedFileStatusTypeCode(),
                            CrbFileTransmissionStatus_.submittedFileStatusTypeCode
                        )
                    );
            }
            if (criteria.getSubmittedFileStatusType() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSubmittedFileStatusType(), CrbFileTransmissionStatus_.submittedFileStatusType)
                    );
            }
        }
        return specification;
    }
}
