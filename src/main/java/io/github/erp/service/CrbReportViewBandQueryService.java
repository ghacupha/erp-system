package io.github.erp.service;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import io.github.erp.domain.CrbReportViewBand;
import io.github.erp.repository.CrbReportViewBandRepository;
import io.github.erp.repository.search.CrbReportViewBandSearchRepository;
import io.github.erp.service.criteria.CrbReportViewBandCriteria;
import io.github.erp.service.dto.CrbReportViewBandDTO;
import io.github.erp.service.mapper.CrbReportViewBandMapper;
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
 * Service for executing complex queries for {@link CrbReportViewBand} entities in the database.
 * The main input is a {@link CrbReportViewBandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbReportViewBandDTO} or a {@link Page} of {@link CrbReportViewBandDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbReportViewBandQueryService extends QueryService<CrbReportViewBand> {

    private final Logger log = LoggerFactory.getLogger(CrbReportViewBandQueryService.class);

    private final CrbReportViewBandRepository crbReportViewBandRepository;

    private final CrbReportViewBandMapper crbReportViewBandMapper;

    private final CrbReportViewBandSearchRepository crbReportViewBandSearchRepository;

    public CrbReportViewBandQueryService(
        CrbReportViewBandRepository crbReportViewBandRepository,
        CrbReportViewBandMapper crbReportViewBandMapper,
        CrbReportViewBandSearchRepository crbReportViewBandSearchRepository
    ) {
        this.crbReportViewBandRepository = crbReportViewBandRepository;
        this.crbReportViewBandMapper = crbReportViewBandMapper;
        this.crbReportViewBandSearchRepository = crbReportViewBandSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbReportViewBandDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbReportViewBandDTO> findByCriteria(CrbReportViewBandCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbReportViewBand> specification = createSpecification(criteria);
        return crbReportViewBandMapper.toDto(crbReportViewBandRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbReportViewBandDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbReportViewBandDTO> findByCriteria(CrbReportViewBandCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbReportViewBand> specification = createSpecification(criteria);
        return crbReportViewBandRepository.findAll(specification, page).map(crbReportViewBandMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbReportViewBandCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbReportViewBand> specification = createSpecification(criteria);
        return crbReportViewBandRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbReportViewBandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbReportViewBand> createSpecification(CrbReportViewBandCriteria criteria) {
        Specification<CrbReportViewBand> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbReportViewBand_.id));
            }
            if (criteria.getReportViewCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportViewCode(), CrbReportViewBand_.reportViewCode));
            }
            if (criteria.getReportViewCategory() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportViewCategory(), CrbReportViewBand_.reportViewCategory));
            }
        }
        return specification;
    }
}
