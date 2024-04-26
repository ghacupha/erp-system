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
