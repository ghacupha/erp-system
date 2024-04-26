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
import io.github.erp.domain.RouAssetListReport;
import io.github.erp.repository.RouAssetListReportRepository;
import io.github.erp.repository.search.RouAssetListReportSearchRepository;
import io.github.erp.service.criteria.RouAssetListReportCriteria;
import io.github.erp.service.dto.RouAssetListReportDTO;
import io.github.erp.service.mapper.RouAssetListReportMapper;
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
 * Service for executing complex queries for {@link RouAssetListReport} entities in the database.
 * The main input is a {@link RouAssetListReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouAssetListReportDTO} or a {@link Page} of {@link RouAssetListReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouAssetListReportQueryService extends QueryService<RouAssetListReport> {

    private final Logger log = LoggerFactory.getLogger(RouAssetListReportQueryService.class);

    private final RouAssetListReportRepository rouAssetListReportRepository;

    private final RouAssetListReportMapper rouAssetListReportMapper;

    private final RouAssetListReportSearchRepository rouAssetListReportSearchRepository;

    public RouAssetListReportQueryService(
        RouAssetListReportRepository rouAssetListReportRepository,
        RouAssetListReportMapper rouAssetListReportMapper,
        RouAssetListReportSearchRepository rouAssetListReportSearchRepository
    ) {
        this.rouAssetListReportRepository = rouAssetListReportRepository;
        this.rouAssetListReportMapper = rouAssetListReportMapper;
        this.rouAssetListReportSearchRepository = rouAssetListReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouAssetListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouAssetListReportDTO> findByCriteria(RouAssetListReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouAssetListReport> specification = createSpecification(criteria);
        return rouAssetListReportMapper.toDto(rouAssetListReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouAssetListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouAssetListReportDTO> findByCriteria(RouAssetListReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouAssetListReport> specification = createSpecification(criteria);
        return rouAssetListReportRepository.findAll(specification, page).map(rouAssetListReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouAssetListReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouAssetListReport> specification = createSpecification(criteria);
        return rouAssetListReportRepository.count(specification);
    }

    /**
     * Function to convert {@link RouAssetListReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouAssetListReport> createSpecification(RouAssetListReportCriteria criteria) {
        Specification<RouAssetListReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouAssetListReport_.id));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), RouAssetListReport_.requestId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), RouAssetListReport_.timeOfRequest));
            }
            if (criteria.getFileChecksum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileChecksum(), RouAssetListReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), RouAssetListReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), RouAssetListReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportParameters(), RouAssetListReport_.reportParameters));
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(RouAssetListReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
