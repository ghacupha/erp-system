package io.github.erp.service;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.domain.CrbReportRequestReasons;
import io.github.erp.repository.CrbReportRequestReasonsRepository;
import io.github.erp.repository.search.CrbReportRequestReasonsSearchRepository;
import io.github.erp.service.criteria.CrbReportRequestReasonsCriteria;
import io.github.erp.service.dto.CrbReportRequestReasonsDTO;
import io.github.erp.service.mapper.CrbReportRequestReasonsMapper;
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
 * Service for executing complex queries for {@link CrbReportRequestReasons} entities in the database.
 * The main input is a {@link CrbReportRequestReasonsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbReportRequestReasonsDTO} or a {@link Page} of {@link CrbReportRequestReasonsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbReportRequestReasonsQueryService extends QueryService<CrbReportRequestReasons> {

    private final Logger log = LoggerFactory.getLogger(CrbReportRequestReasonsQueryService.class);

    private final CrbReportRequestReasonsRepository crbReportRequestReasonsRepository;

    private final CrbReportRequestReasonsMapper crbReportRequestReasonsMapper;

    private final CrbReportRequestReasonsSearchRepository crbReportRequestReasonsSearchRepository;

    public CrbReportRequestReasonsQueryService(
        CrbReportRequestReasonsRepository crbReportRequestReasonsRepository,
        CrbReportRequestReasonsMapper crbReportRequestReasonsMapper,
        CrbReportRequestReasonsSearchRepository crbReportRequestReasonsSearchRepository
    ) {
        this.crbReportRequestReasonsRepository = crbReportRequestReasonsRepository;
        this.crbReportRequestReasonsMapper = crbReportRequestReasonsMapper;
        this.crbReportRequestReasonsSearchRepository = crbReportRequestReasonsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbReportRequestReasonsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbReportRequestReasonsDTO> findByCriteria(CrbReportRequestReasonsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbReportRequestReasons> specification = createSpecification(criteria);
        return crbReportRequestReasonsMapper.toDto(crbReportRequestReasonsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbReportRequestReasonsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbReportRequestReasonsDTO> findByCriteria(CrbReportRequestReasonsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbReportRequestReasons> specification = createSpecification(criteria);
        return crbReportRequestReasonsRepository.findAll(specification, page).map(crbReportRequestReasonsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbReportRequestReasonsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbReportRequestReasons> specification = createSpecification(criteria);
        return crbReportRequestReasonsRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbReportRequestReasonsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbReportRequestReasons> createSpecification(CrbReportRequestReasonsCriteria criteria) {
        Specification<CrbReportRequestReasons> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbReportRequestReasons_.id));
            }
            if (criteria.getCreditReportRequestReasonTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCreditReportRequestReasonTypeCode(),
                            CrbReportRequestReasons_.creditReportRequestReasonTypeCode
                        )
                    );
            }
            if (criteria.getCreditReportRequestReasonType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCreditReportRequestReasonType(),
                            CrbReportRequestReasons_.creditReportRequestReasonType
                        )
                    );
            }
        }
        return specification;
    }
}
