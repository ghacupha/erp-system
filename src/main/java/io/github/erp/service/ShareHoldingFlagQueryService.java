package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.ShareHoldingFlag;
import io.github.erp.repository.ShareHoldingFlagRepository;
import io.github.erp.repository.search.ShareHoldingFlagSearchRepository;
import io.github.erp.service.criteria.ShareHoldingFlagCriteria;
import io.github.erp.service.dto.ShareHoldingFlagDTO;
import io.github.erp.service.mapper.ShareHoldingFlagMapper;
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
 * Service for executing complex queries for {@link ShareHoldingFlag} entities in the database.
 * The main input is a {@link ShareHoldingFlagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShareHoldingFlagDTO} or a {@link Page} of {@link ShareHoldingFlagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShareHoldingFlagQueryService extends QueryService<ShareHoldingFlag> {

    private final Logger log = LoggerFactory.getLogger(ShareHoldingFlagQueryService.class);

    private final ShareHoldingFlagRepository shareHoldingFlagRepository;

    private final ShareHoldingFlagMapper shareHoldingFlagMapper;

    private final ShareHoldingFlagSearchRepository shareHoldingFlagSearchRepository;

    public ShareHoldingFlagQueryService(
        ShareHoldingFlagRepository shareHoldingFlagRepository,
        ShareHoldingFlagMapper shareHoldingFlagMapper,
        ShareHoldingFlagSearchRepository shareHoldingFlagSearchRepository
    ) {
        this.shareHoldingFlagRepository = shareHoldingFlagRepository;
        this.shareHoldingFlagMapper = shareHoldingFlagMapper;
        this.shareHoldingFlagSearchRepository = shareHoldingFlagSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ShareHoldingFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShareHoldingFlagDTO> findByCriteria(ShareHoldingFlagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShareHoldingFlag> specification = createSpecification(criteria);
        return shareHoldingFlagMapper.toDto(shareHoldingFlagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShareHoldingFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShareHoldingFlagDTO> findByCriteria(ShareHoldingFlagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShareHoldingFlag> specification = createSpecification(criteria);
        return shareHoldingFlagRepository.findAll(specification, page).map(shareHoldingFlagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShareHoldingFlagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShareHoldingFlag> specification = createSpecification(criteria);
        return shareHoldingFlagRepository.count(specification);
    }

    /**
     * Function to convert {@link ShareHoldingFlagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShareHoldingFlag> createSpecification(ShareHoldingFlagCriteria criteria) {
        Specification<ShareHoldingFlag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShareHoldingFlag_.id));
            }
            if (criteria.getShareholdingFlagTypeCode() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getShareholdingFlagTypeCode(), ShareHoldingFlag_.shareholdingFlagTypeCode)
                    );
            }
            if (criteria.getShareholdingFlagType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getShareholdingFlagType(), ShareHoldingFlag_.shareholdingFlagType));
            }
        }
        return specification;
    }
}
