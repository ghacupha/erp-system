package io.github.erp.service;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.WIPListReport;
import io.github.erp.repository.WIPListReportRepository;
import io.github.erp.repository.search.WIPListReportSearchRepository;
import io.github.erp.service.criteria.WIPListReportCriteria;
import io.github.erp.service.dto.WIPListReportDTO;
import io.github.erp.service.mapper.WIPListReportMapper;
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
 * Service for executing complex queries for {@link WIPListReport} entities in the database.
 * The main input is a {@link WIPListReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WIPListReportDTO} or a {@link Page} of {@link WIPListReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WIPListReportQueryService extends QueryService<WIPListReport> {

    private final Logger log = LoggerFactory.getLogger(WIPListReportQueryService.class);

    private final WIPListReportRepository wIPListReportRepository;

    private final WIPListReportMapper wIPListReportMapper;

    private final WIPListReportSearchRepository wIPListReportSearchRepository;

    public WIPListReportQueryService(
        WIPListReportRepository wIPListReportRepository,
        WIPListReportMapper wIPListReportMapper,
        WIPListReportSearchRepository wIPListReportSearchRepository
    ) {
        this.wIPListReportRepository = wIPListReportRepository;
        this.wIPListReportMapper = wIPListReportMapper;
        this.wIPListReportSearchRepository = wIPListReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WIPListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WIPListReportDTO> findByCriteria(WIPListReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WIPListReport> specification = createSpecification(criteria);
        return wIPListReportMapper.toDto(wIPListReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WIPListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WIPListReportDTO> findByCriteria(WIPListReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WIPListReport> specification = createSpecification(criteria);
        return wIPListReportRepository.findAll(specification, page).map(wIPListReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WIPListReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WIPListReport> specification = createSpecification(criteria);
        return wIPListReportRepository.count(specification);
    }

    /**
     * Function to convert {@link WIPListReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WIPListReport> createSpecification(WIPListReportCriteria criteria) {
        Specification<WIPListReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WIPListReport_.id));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), WIPListReport_.timeOfRequest));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), WIPListReport_.requestId));
            }
            if (criteria.getFileChecksum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileChecksum(), WIPListReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), WIPListReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), WIPListReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportParameters(), WIPListReport_.reportParameters));
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(WIPListReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
