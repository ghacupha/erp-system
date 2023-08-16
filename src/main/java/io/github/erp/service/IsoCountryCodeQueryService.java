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
import io.github.erp.domain.IsoCountryCode;
import io.github.erp.repository.IsoCountryCodeRepository;
import io.github.erp.repository.search.IsoCountryCodeSearchRepository;
import io.github.erp.service.criteria.IsoCountryCodeCriteria;
import io.github.erp.service.dto.IsoCountryCodeDTO;
import io.github.erp.service.mapper.IsoCountryCodeMapper;
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
 * Service for executing complex queries for {@link IsoCountryCode} entities in the database.
 * The main input is a {@link IsoCountryCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IsoCountryCodeDTO} or a {@link Page} of {@link IsoCountryCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IsoCountryCodeQueryService extends QueryService<IsoCountryCode> {

    private final Logger log = LoggerFactory.getLogger(IsoCountryCodeQueryService.class);

    private final IsoCountryCodeRepository isoCountryCodeRepository;

    private final IsoCountryCodeMapper isoCountryCodeMapper;

    private final IsoCountryCodeSearchRepository isoCountryCodeSearchRepository;

    public IsoCountryCodeQueryService(
        IsoCountryCodeRepository isoCountryCodeRepository,
        IsoCountryCodeMapper isoCountryCodeMapper,
        IsoCountryCodeSearchRepository isoCountryCodeSearchRepository
    ) {
        this.isoCountryCodeRepository = isoCountryCodeRepository;
        this.isoCountryCodeMapper = isoCountryCodeMapper;
        this.isoCountryCodeSearchRepository = isoCountryCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link IsoCountryCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IsoCountryCodeDTO> findByCriteria(IsoCountryCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IsoCountryCode> specification = createSpecification(criteria);
        return isoCountryCodeMapper.toDto(isoCountryCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IsoCountryCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IsoCountryCodeDTO> findByCriteria(IsoCountryCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IsoCountryCode> specification = createSpecification(criteria);
        return isoCountryCodeRepository.findAll(specification, page).map(isoCountryCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IsoCountryCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IsoCountryCode> specification = createSpecification(criteria);
        return isoCountryCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link IsoCountryCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IsoCountryCode> createSpecification(IsoCountryCodeCriteria criteria) {
        Specification<IsoCountryCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IsoCountryCode_.id));
            }
            if (criteria.getCountryCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountryCode(), IsoCountryCode_.countryCode));
            }
            if (criteria.getCountryDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCountryDescription(), IsoCountryCode_.countryDescription));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(IsoCountryCode_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
