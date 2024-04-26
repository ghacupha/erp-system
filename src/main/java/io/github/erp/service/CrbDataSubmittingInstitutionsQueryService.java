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
import io.github.erp.domain.CrbDataSubmittingInstitutions;
import io.github.erp.repository.CrbDataSubmittingInstitutionsRepository;
import io.github.erp.repository.search.CrbDataSubmittingInstitutionsSearchRepository;
import io.github.erp.service.criteria.CrbDataSubmittingInstitutionsCriteria;
import io.github.erp.service.dto.CrbDataSubmittingInstitutionsDTO;
import io.github.erp.service.mapper.CrbDataSubmittingInstitutionsMapper;
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
 * Service for executing complex queries for {@link CrbDataSubmittingInstitutions} entities in the database.
 * The main input is a {@link CrbDataSubmittingInstitutionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbDataSubmittingInstitutionsDTO} or a {@link Page} of {@link CrbDataSubmittingInstitutionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbDataSubmittingInstitutionsQueryService extends QueryService<CrbDataSubmittingInstitutions> {

    private final Logger log = LoggerFactory.getLogger(CrbDataSubmittingInstitutionsQueryService.class);

    private final CrbDataSubmittingInstitutionsRepository crbDataSubmittingInstitutionsRepository;

    private final CrbDataSubmittingInstitutionsMapper crbDataSubmittingInstitutionsMapper;

    private final CrbDataSubmittingInstitutionsSearchRepository crbDataSubmittingInstitutionsSearchRepository;

    public CrbDataSubmittingInstitutionsQueryService(
        CrbDataSubmittingInstitutionsRepository crbDataSubmittingInstitutionsRepository,
        CrbDataSubmittingInstitutionsMapper crbDataSubmittingInstitutionsMapper,
        CrbDataSubmittingInstitutionsSearchRepository crbDataSubmittingInstitutionsSearchRepository
    ) {
        this.crbDataSubmittingInstitutionsRepository = crbDataSubmittingInstitutionsRepository;
        this.crbDataSubmittingInstitutionsMapper = crbDataSubmittingInstitutionsMapper;
        this.crbDataSubmittingInstitutionsSearchRepository = crbDataSubmittingInstitutionsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbDataSubmittingInstitutionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbDataSubmittingInstitutionsDTO> findByCriteria(CrbDataSubmittingInstitutionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbDataSubmittingInstitutions> specification = createSpecification(criteria);
        return crbDataSubmittingInstitutionsMapper.toDto(crbDataSubmittingInstitutionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbDataSubmittingInstitutionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbDataSubmittingInstitutionsDTO> findByCriteria(CrbDataSubmittingInstitutionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbDataSubmittingInstitutions> specification = createSpecification(criteria);
        return crbDataSubmittingInstitutionsRepository.findAll(specification, page).map(crbDataSubmittingInstitutionsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbDataSubmittingInstitutionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbDataSubmittingInstitutions> specification = createSpecification(criteria);
        return crbDataSubmittingInstitutionsRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbDataSubmittingInstitutionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbDataSubmittingInstitutions> createSpecification(CrbDataSubmittingInstitutionsCriteria criteria) {
        Specification<CrbDataSubmittingInstitutions> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbDataSubmittingInstitutions_.id));
            }
            if (criteria.getInstitutionCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getInstitutionCode(), CrbDataSubmittingInstitutions_.institutionCode)
                    );
            }
            if (criteria.getInstitutionName() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getInstitutionName(), CrbDataSubmittingInstitutions_.institutionName)
                    );
            }
            if (criteria.getInstitutionCategory() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getInstitutionCategory(), CrbDataSubmittingInstitutions_.institutionCategory)
                    );
            }
        }
        return specification;
    }
}
