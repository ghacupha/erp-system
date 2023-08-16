package io.github.erp.service;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.5
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
import io.github.erp.domain.AgencyNotice;
import io.github.erp.repository.AgencyNoticeRepository;
import io.github.erp.repository.search.AgencyNoticeSearchRepository;
import io.github.erp.service.criteria.AgencyNoticeCriteria;
import io.github.erp.service.dto.AgencyNoticeDTO;
import io.github.erp.service.mapper.AgencyNoticeMapper;
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
 * Service for executing complex queries for {@link AgencyNotice} entities in the database.
 * The main input is a {@link AgencyNoticeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AgencyNoticeDTO} or a {@link Page} of {@link AgencyNoticeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgencyNoticeQueryService extends QueryService<AgencyNotice> {

    private final Logger log = LoggerFactory.getLogger(AgencyNoticeQueryService.class);

    private final AgencyNoticeRepository agencyNoticeRepository;

    private final AgencyNoticeMapper agencyNoticeMapper;

    private final AgencyNoticeSearchRepository agencyNoticeSearchRepository;

    public AgencyNoticeQueryService(
        AgencyNoticeRepository agencyNoticeRepository,
        AgencyNoticeMapper agencyNoticeMapper,
        AgencyNoticeSearchRepository agencyNoticeSearchRepository
    ) {
        this.agencyNoticeRepository = agencyNoticeRepository;
        this.agencyNoticeMapper = agencyNoticeMapper;
        this.agencyNoticeSearchRepository = agencyNoticeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AgencyNoticeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AgencyNoticeDTO> findByCriteria(AgencyNoticeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AgencyNotice> specification = createSpecification(criteria);
        return agencyNoticeMapper.toDto(agencyNoticeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AgencyNoticeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AgencyNoticeDTO> findByCriteria(AgencyNoticeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AgencyNotice> specification = createSpecification(criteria);
        return agencyNoticeRepository.findAll(specification, page).map(agencyNoticeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgencyNoticeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AgencyNotice> specification = createSpecification(criteria);
        return agencyNoticeRepository.count(specification);
    }

    /**
     * Function to convert {@link AgencyNoticeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AgencyNotice> createSpecification(AgencyNoticeCriteria criteria) {
        Specification<AgencyNotice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AgencyNotice_.id));
            }
            if (criteria.getReferenceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReferenceNumber(), AgencyNotice_.referenceNumber));
            }
            if (criteria.getReferenceDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReferenceDate(), AgencyNotice_.referenceDate));
            }
            if (criteria.getAssessmentAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssessmentAmount(), AgencyNotice_.assessmentAmount));
            }
            if (criteria.getAgencyStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getAgencyStatus(), AgencyNotice_.agencyStatus));
            }
            if (criteria.getCorrespondentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCorrespondentsId(),
                            root -> root.join(AgencyNotice_.correspondents, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(AgencyNotice_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getAssessorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssessorId(),
                            root -> root.join(AgencyNotice_.assessor, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AgencyNotice_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(AgencyNotice_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
