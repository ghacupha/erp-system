package io.github.erp.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.RouAccountBalanceReport;
import io.github.erp.repository.RouAccountBalanceReportRepository;
import io.github.erp.repository.search.RouAccountBalanceReportSearchRepository;
import io.github.erp.service.criteria.RouAccountBalanceReportCriteria;
import io.github.erp.service.dto.RouAccountBalanceReportDTO;
import io.github.erp.service.mapper.RouAccountBalanceReportMapper;
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
 * Service for executing complex queries for {@link RouAccountBalanceReport} entities in the database.
 * The main input is a {@link RouAccountBalanceReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouAccountBalanceReportDTO} or a {@link Page} of {@link RouAccountBalanceReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouAccountBalanceReportQueryService extends QueryService<RouAccountBalanceReport> {

    private final Logger log = LoggerFactory.getLogger(RouAccountBalanceReportQueryService.class);

    private final RouAccountBalanceReportRepository rouAccountBalanceReportRepository;

    private final RouAccountBalanceReportMapper rouAccountBalanceReportMapper;

    private final RouAccountBalanceReportSearchRepository rouAccountBalanceReportSearchRepository;

    public RouAccountBalanceReportQueryService(
        RouAccountBalanceReportRepository rouAccountBalanceReportRepository,
        RouAccountBalanceReportMapper rouAccountBalanceReportMapper,
        RouAccountBalanceReportSearchRepository rouAccountBalanceReportSearchRepository
    ) {
        this.rouAccountBalanceReportRepository = rouAccountBalanceReportRepository;
        this.rouAccountBalanceReportMapper = rouAccountBalanceReportMapper;
        this.rouAccountBalanceReportSearchRepository = rouAccountBalanceReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouAccountBalanceReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouAccountBalanceReportDTO> findByCriteria(RouAccountBalanceReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouAccountBalanceReport> specification = createSpecification(criteria);
        return rouAccountBalanceReportMapper.toDto(rouAccountBalanceReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouAccountBalanceReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouAccountBalanceReportDTO> findByCriteria(RouAccountBalanceReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouAccountBalanceReport> specification = createSpecification(criteria);
        return rouAccountBalanceReportRepository.findAll(specification, page).map(rouAccountBalanceReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouAccountBalanceReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouAccountBalanceReport> specification = createSpecification(criteria);
        return rouAccountBalanceReportRepository.count(specification);
    }

    /**
     * Function to convert {@link RouAccountBalanceReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouAccountBalanceReport> createSpecification(RouAccountBalanceReportCriteria criteria) {
        Specification<RouAccountBalanceReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouAccountBalanceReport_.id));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), RouAccountBalanceReport_.requestId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), RouAccountBalanceReport_.timeOfRequest));
            }
            if (criteria.getReportIsCompiled() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getReportIsCompiled(), RouAccountBalanceReport_.reportIsCompiled));
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileChecksum(), RouAccountBalanceReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), RouAccountBalanceReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), RouAccountBalanceReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportParameters(), RouAccountBalanceReport_.reportParameters));
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(RouAccountBalanceReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getReportingMonthId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportingMonthId(),
                            root -> root.join(RouAccountBalanceReport_.reportingMonth, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
