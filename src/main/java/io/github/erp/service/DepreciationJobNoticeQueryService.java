package io.github.erp.service;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.domain.DepreciationJobNotice;
import io.github.erp.repository.DepreciationJobNoticeRepository;
import io.github.erp.repository.search.DepreciationJobNoticeSearchRepository;
import io.github.erp.service.criteria.DepreciationJobNoticeCriteria;
import io.github.erp.service.dto.DepreciationJobNoticeDTO;
import io.github.erp.service.mapper.DepreciationJobNoticeMapper;
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
 * Service for executing complex queries for {@link DepreciationJobNotice} entities in the database.
 * The main input is a {@link DepreciationJobNoticeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepreciationJobNoticeDTO} or a {@link Page} of {@link DepreciationJobNoticeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepreciationJobNoticeQueryService extends QueryService<DepreciationJobNotice> {

    private final Logger log = LoggerFactory.getLogger(DepreciationJobNoticeQueryService.class);

    private final DepreciationJobNoticeRepository depreciationJobNoticeRepository;

    private final DepreciationJobNoticeMapper depreciationJobNoticeMapper;

    private final DepreciationJobNoticeSearchRepository depreciationJobNoticeSearchRepository;

    public DepreciationJobNoticeQueryService(
        DepreciationJobNoticeRepository depreciationJobNoticeRepository,
        DepreciationJobNoticeMapper depreciationJobNoticeMapper,
        DepreciationJobNoticeSearchRepository depreciationJobNoticeSearchRepository
    ) {
        this.depreciationJobNoticeRepository = depreciationJobNoticeRepository;
        this.depreciationJobNoticeMapper = depreciationJobNoticeMapper;
        this.depreciationJobNoticeSearchRepository = depreciationJobNoticeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DepreciationJobNoticeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepreciationJobNoticeDTO> findByCriteria(DepreciationJobNoticeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DepreciationJobNotice> specification = createSpecification(criteria);
        return depreciationJobNoticeMapper.toDto(depreciationJobNoticeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepreciationJobNoticeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepreciationJobNoticeDTO> findByCriteria(DepreciationJobNoticeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepreciationJobNotice> specification = createSpecification(criteria);
        return depreciationJobNoticeRepository.findAll(specification, page).map(depreciationJobNoticeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepreciationJobNoticeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DepreciationJobNotice> specification = createSpecification(criteria);
        return depreciationJobNoticeRepository.count(specification);
    }

    /**
     * Function to convert {@link DepreciationJobNoticeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepreciationJobNotice> createSpecification(DepreciationJobNoticeCriteria criteria) {
        Specification<DepreciationJobNotice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DepreciationJobNotice_.id));
            }
            if (criteria.getEventNarrative() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getEventNarrative(), DepreciationJobNotice_.eventNarrative));
            }
            if (criteria.getEventTimeStamp() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getEventTimeStamp(), DepreciationJobNotice_.eventTimeStamp));
            }
            if (criteria.getDepreciationNoticeStatus() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDepreciationNoticeStatus(), DepreciationJobNotice_.depreciationNoticeStatus)
                    );
            }
            if (criteria.getSourceModule() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSourceModule(), DepreciationJobNotice_.sourceModule));
            }
            if (criteria.getSourceEntity() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSourceEntity(), DepreciationJobNotice_.sourceEntity));
            }
            if (criteria.getErrorCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getErrorCode(), DepreciationJobNotice_.errorCode));
            }
            if (criteria.getUserAction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserAction(), DepreciationJobNotice_.userAction));
            }
            if (criteria.getDepreciationJobId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationJobId(),
                            root -> root.join(DepreciationJobNotice_.depreciationJob, JoinType.LEFT).get(DepreciationJob_.id)
                        )
                    );
            }
            if (criteria.getDepreciationBatchSequenceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationBatchSequenceId(),
                            root ->
                                root
                                    .join(DepreciationJobNotice_.depreciationBatchSequence, JoinType.LEFT)
                                    .get(DepreciationBatchSequence_.id)
                        )
                    );
            }
            if (criteria.getDepreciationPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationPeriodId(),
                            root -> root.join(DepreciationJobNotice_.depreciationPeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(DepreciationJobNotice_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getUniversallyUniqueMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUniversallyUniqueMappingId(),
                            root ->
                                root.join(DepreciationJobNotice_.universallyUniqueMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getSuperintendedId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperintendedId(),
                            root -> root.join(DepreciationJobNotice_.superintended, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
