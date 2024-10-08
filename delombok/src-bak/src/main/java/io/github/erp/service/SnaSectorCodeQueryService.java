package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.SnaSectorCode;
import io.github.erp.repository.SnaSectorCodeRepository;
import io.github.erp.repository.search.SnaSectorCodeSearchRepository;
import io.github.erp.service.criteria.SnaSectorCodeCriteria;
import io.github.erp.service.dto.SnaSectorCodeDTO;
import io.github.erp.service.mapper.SnaSectorCodeMapper;
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
 * Service for executing complex queries for {@link SnaSectorCode} entities in the database.
 * The main input is a {@link SnaSectorCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SnaSectorCodeDTO} or a {@link Page} of {@link SnaSectorCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SnaSectorCodeQueryService extends QueryService<SnaSectorCode> {

    private final Logger log = LoggerFactory.getLogger(SnaSectorCodeQueryService.class);

    private final SnaSectorCodeRepository snaSectorCodeRepository;

    private final SnaSectorCodeMapper snaSectorCodeMapper;

    private final SnaSectorCodeSearchRepository snaSectorCodeSearchRepository;

    public SnaSectorCodeQueryService(
        SnaSectorCodeRepository snaSectorCodeRepository,
        SnaSectorCodeMapper snaSectorCodeMapper,
        SnaSectorCodeSearchRepository snaSectorCodeSearchRepository
    ) {
        this.snaSectorCodeRepository = snaSectorCodeRepository;
        this.snaSectorCodeMapper = snaSectorCodeMapper;
        this.snaSectorCodeSearchRepository = snaSectorCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SnaSectorCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SnaSectorCodeDTO> findByCriteria(SnaSectorCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SnaSectorCode> specification = createSpecification(criteria);
        return snaSectorCodeMapper.toDto(snaSectorCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SnaSectorCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SnaSectorCodeDTO> findByCriteria(SnaSectorCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SnaSectorCode> specification = createSpecification(criteria);
        return snaSectorCodeRepository.findAll(specification, page).map(snaSectorCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SnaSectorCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SnaSectorCode> specification = createSpecification(criteria);
        return snaSectorCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link SnaSectorCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SnaSectorCode> createSpecification(SnaSectorCodeCriteria criteria) {
        Specification<SnaSectorCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SnaSectorCode_.id));
            }
            if (criteria.getSectorTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSectorTypeCode(), SnaSectorCode_.sectorTypeCode));
            }
            if (criteria.getMainSectorCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMainSectorCode(), SnaSectorCode_.mainSectorCode));
            }
            if (criteria.getMainSectorTypeName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMainSectorTypeName(), SnaSectorCode_.mainSectorTypeName));
            }
            if (criteria.getSubSectorCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubSectorCode(), SnaSectorCode_.subSectorCode));
            }
            if (criteria.getSubSectorName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubSectorName(), SnaSectorCode_.subSectorName));
            }
            if (criteria.getSubSubSectorCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSubSubSectorCode(), SnaSectorCode_.subSubSectorCode));
            }
            if (criteria.getSubSubSectorName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSubSubSectorName(), SnaSectorCode_.subSubSectorName));
            }
        }
        return specification;
    }
}
