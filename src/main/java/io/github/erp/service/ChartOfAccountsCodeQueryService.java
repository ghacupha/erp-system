package io.github.erp.service;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.domain.ChartOfAccountsCode;
import io.github.erp.repository.ChartOfAccountsCodeRepository;
import io.github.erp.repository.search.ChartOfAccountsCodeSearchRepository;
import io.github.erp.service.criteria.ChartOfAccountsCodeCriteria;
import io.github.erp.service.dto.ChartOfAccountsCodeDTO;
import io.github.erp.service.mapper.ChartOfAccountsCodeMapper;
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
 * Service for executing complex queries for {@link ChartOfAccountsCode} entities in the database.
 * The main input is a {@link ChartOfAccountsCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChartOfAccountsCodeDTO} or a {@link Page} of {@link ChartOfAccountsCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChartOfAccountsCodeQueryService extends QueryService<ChartOfAccountsCode> {

    private final Logger log = LoggerFactory.getLogger(ChartOfAccountsCodeQueryService.class);

    private final ChartOfAccountsCodeRepository chartOfAccountsCodeRepository;

    private final ChartOfAccountsCodeMapper chartOfAccountsCodeMapper;

    private final ChartOfAccountsCodeSearchRepository chartOfAccountsCodeSearchRepository;

    public ChartOfAccountsCodeQueryService(
        ChartOfAccountsCodeRepository chartOfAccountsCodeRepository,
        ChartOfAccountsCodeMapper chartOfAccountsCodeMapper,
        ChartOfAccountsCodeSearchRepository chartOfAccountsCodeSearchRepository
    ) {
        this.chartOfAccountsCodeRepository = chartOfAccountsCodeRepository;
        this.chartOfAccountsCodeMapper = chartOfAccountsCodeMapper;
        this.chartOfAccountsCodeSearchRepository = chartOfAccountsCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ChartOfAccountsCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChartOfAccountsCodeDTO> findByCriteria(ChartOfAccountsCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChartOfAccountsCode> specification = createSpecification(criteria);
        return chartOfAccountsCodeMapper.toDto(chartOfAccountsCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChartOfAccountsCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChartOfAccountsCodeDTO> findByCriteria(ChartOfAccountsCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChartOfAccountsCode> specification = createSpecification(criteria);
        return chartOfAccountsCodeRepository.findAll(specification, page).map(chartOfAccountsCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChartOfAccountsCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChartOfAccountsCode> specification = createSpecification(criteria);
        return chartOfAccountsCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link ChartOfAccountsCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChartOfAccountsCode> createSpecification(ChartOfAccountsCodeCriteria criteria) {
        Specification<ChartOfAccountsCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ChartOfAccountsCode_.id));
            }
            if (criteria.getChartOfAccountsCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getChartOfAccountsCode(), ChartOfAccountsCode_.chartOfAccountsCode)
                    );
            }
            if (criteria.getChartOfAccountsClass() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getChartOfAccountsClass(), ChartOfAccountsCode_.chartOfAccountsClass)
                    );
            }
        }
        return specification;
    }
}
