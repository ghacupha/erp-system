package io.github.erp.service;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.domain.SubCountyCode;
import io.github.erp.repository.SubCountyCodeRepository;
import io.github.erp.repository.search.SubCountyCodeSearchRepository;
import io.github.erp.service.criteria.SubCountyCodeCriteria;
import io.github.erp.service.dto.SubCountyCodeDTO;
import io.github.erp.service.mapper.SubCountyCodeMapper;
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
 * Service for executing complex queries for {@link SubCountyCode} entities in the database.
 * The main input is a {@link SubCountyCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubCountyCodeDTO} or a {@link Page} of {@link SubCountyCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubCountyCodeQueryService extends QueryService<SubCountyCode> {

    private final Logger log = LoggerFactory.getLogger(SubCountyCodeQueryService.class);

    private final SubCountyCodeRepository subCountyCodeRepository;

    private final SubCountyCodeMapper subCountyCodeMapper;

    private final SubCountyCodeSearchRepository subCountyCodeSearchRepository;

    public SubCountyCodeQueryService(
        SubCountyCodeRepository subCountyCodeRepository,
        SubCountyCodeMapper subCountyCodeMapper,
        SubCountyCodeSearchRepository subCountyCodeSearchRepository
    ) {
        this.subCountyCodeRepository = subCountyCodeRepository;
        this.subCountyCodeMapper = subCountyCodeMapper;
        this.subCountyCodeSearchRepository = subCountyCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SubCountyCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubCountyCodeDTO> findByCriteria(SubCountyCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubCountyCode> specification = createSpecification(criteria);
        return subCountyCodeMapper.toDto(subCountyCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubCountyCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubCountyCodeDTO> findByCriteria(SubCountyCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubCountyCode> specification = createSpecification(criteria);
        return subCountyCodeRepository.findAll(specification, page).map(subCountyCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubCountyCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubCountyCode> specification = createSpecification(criteria);
        return subCountyCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link SubCountyCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubCountyCode> createSpecification(SubCountyCodeCriteria criteria) {
        Specification<SubCountyCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubCountyCode_.id));
            }
            if (criteria.getCountyCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountyCode(), SubCountyCode_.countyCode));
            }
            if (criteria.getCountyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountyName(), SubCountyCode_.countyName));
            }
            if (criteria.getSubCountyCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubCountyCode(), SubCountyCode_.subCountyCode));
            }
            if (criteria.getSubCountyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubCountyName(), SubCountyCode_.subCountyName));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(SubCountyCode_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
