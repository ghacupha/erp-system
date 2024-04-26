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
import io.github.erp.domain.EmploymentTerms;
import io.github.erp.repository.EmploymentTermsRepository;
import io.github.erp.repository.search.EmploymentTermsSearchRepository;
import io.github.erp.service.criteria.EmploymentTermsCriteria;
import io.github.erp.service.dto.EmploymentTermsDTO;
import io.github.erp.service.mapper.EmploymentTermsMapper;
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
 * Service for executing complex queries for {@link EmploymentTerms} entities in the database.
 * The main input is a {@link EmploymentTermsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmploymentTermsDTO} or a {@link Page} of {@link EmploymentTermsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmploymentTermsQueryService extends QueryService<EmploymentTerms> {

    private final Logger log = LoggerFactory.getLogger(EmploymentTermsQueryService.class);

    private final EmploymentTermsRepository employmentTermsRepository;

    private final EmploymentTermsMapper employmentTermsMapper;

    private final EmploymentTermsSearchRepository employmentTermsSearchRepository;

    public EmploymentTermsQueryService(
        EmploymentTermsRepository employmentTermsRepository,
        EmploymentTermsMapper employmentTermsMapper,
        EmploymentTermsSearchRepository employmentTermsSearchRepository
    ) {
        this.employmentTermsRepository = employmentTermsRepository;
        this.employmentTermsMapper = employmentTermsMapper;
        this.employmentTermsSearchRepository = employmentTermsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link EmploymentTermsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmploymentTermsDTO> findByCriteria(EmploymentTermsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmploymentTerms> specification = createSpecification(criteria);
        return employmentTermsMapper.toDto(employmentTermsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmploymentTermsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmploymentTermsDTO> findByCriteria(EmploymentTermsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmploymentTerms> specification = createSpecification(criteria);
        return employmentTermsRepository.findAll(specification, page).map(employmentTermsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmploymentTermsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmploymentTerms> specification = createSpecification(criteria);
        return employmentTermsRepository.count(specification);
    }

    /**
     * Function to convert {@link EmploymentTermsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmploymentTerms> createSpecification(EmploymentTermsCriteria criteria) {
        Specification<EmploymentTerms> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmploymentTerms_.id));
            }
            if (criteria.getEmploymentTermsCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getEmploymentTermsCode(), EmploymentTerms_.employmentTermsCode));
            }
            if (criteria.getEmploymentTermsStatus() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getEmploymentTermsStatus(), EmploymentTerms_.employmentTermsStatus)
                    );
            }
        }
        return specification;
    }
}
