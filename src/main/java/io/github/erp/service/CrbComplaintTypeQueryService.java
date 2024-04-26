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
import io.github.erp.domain.CrbComplaintType;
import io.github.erp.repository.CrbComplaintTypeRepository;
import io.github.erp.repository.search.CrbComplaintTypeSearchRepository;
import io.github.erp.service.criteria.CrbComplaintTypeCriteria;
import io.github.erp.service.dto.CrbComplaintTypeDTO;
import io.github.erp.service.mapper.CrbComplaintTypeMapper;
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
 * Service for executing complex queries for {@link CrbComplaintType} entities in the database.
 * The main input is a {@link CrbComplaintTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbComplaintTypeDTO} or a {@link Page} of {@link CrbComplaintTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbComplaintTypeQueryService extends QueryService<CrbComplaintType> {

    private final Logger log = LoggerFactory.getLogger(CrbComplaintTypeQueryService.class);

    private final CrbComplaintTypeRepository crbComplaintTypeRepository;

    private final CrbComplaintTypeMapper crbComplaintTypeMapper;

    private final CrbComplaintTypeSearchRepository crbComplaintTypeSearchRepository;

    public CrbComplaintTypeQueryService(
        CrbComplaintTypeRepository crbComplaintTypeRepository,
        CrbComplaintTypeMapper crbComplaintTypeMapper,
        CrbComplaintTypeSearchRepository crbComplaintTypeSearchRepository
    ) {
        this.crbComplaintTypeRepository = crbComplaintTypeRepository;
        this.crbComplaintTypeMapper = crbComplaintTypeMapper;
        this.crbComplaintTypeSearchRepository = crbComplaintTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbComplaintTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbComplaintTypeDTO> findByCriteria(CrbComplaintTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbComplaintType> specification = createSpecification(criteria);
        return crbComplaintTypeMapper.toDto(crbComplaintTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbComplaintTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbComplaintTypeDTO> findByCriteria(CrbComplaintTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbComplaintType> specification = createSpecification(criteria);
        return crbComplaintTypeRepository.findAll(specification, page).map(crbComplaintTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbComplaintTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbComplaintType> specification = createSpecification(criteria);
        return crbComplaintTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbComplaintTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbComplaintType> createSpecification(CrbComplaintTypeCriteria criteria) {
        Specification<CrbComplaintType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbComplaintType_.id));
            }
            if (criteria.getComplaintTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getComplaintTypeCode(), CrbComplaintType_.complaintTypeCode));
            }
            if (criteria.getComplaintType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComplaintType(), CrbComplaintType_.complaintType));
            }
        }
        return specification;
    }
}
