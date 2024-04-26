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
import io.github.erp.domain.CrbNatureOfInformation;
import io.github.erp.repository.CrbNatureOfInformationRepository;
import io.github.erp.repository.search.CrbNatureOfInformationSearchRepository;
import io.github.erp.service.criteria.CrbNatureOfInformationCriteria;
import io.github.erp.service.dto.CrbNatureOfInformationDTO;
import io.github.erp.service.mapper.CrbNatureOfInformationMapper;
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
 * Service for executing complex queries for {@link CrbNatureOfInformation} entities in the database.
 * The main input is a {@link CrbNatureOfInformationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbNatureOfInformationDTO} or a {@link Page} of {@link CrbNatureOfInformationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbNatureOfInformationQueryService extends QueryService<CrbNatureOfInformation> {

    private final Logger log = LoggerFactory.getLogger(CrbNatureOfInformationQueryService.class);

    private final CrbNatureOfInformationRepository crbNatureOfInformationRepository;

    private final CrbNatureOfInformationMapper crbNatureOfInformationMapper;

    private final CrbNatureOfInformationSearchRepository crbNatureOfInformationSearchRepository;

    public CrbNatureOfInformationQueryService(
        CrbNatureOfInformationRepository crbNatureOfInformationRepository,
        CrbNatureOfInformationMapper crbNatureOfInformationMapper,
        CrbNatureOfInformationSearchRepository crbNatureOfInformationSearchRepository
    ) {
        this.crbNatureOfInformationRepository = crbNatureOfInformationRepository;
        this.crbNatureOfInformationMapper = crbNatureOfInformationMapper;
        this.crbNatureOfInformationSearchRepository = crbNatureOfInformationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbNatureOfInformationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbNatureOfInformationDTO> findByCriteria(CrbNatureOfInformationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbNatureOfInformation> specification = createSpecification(criteria);
        return crbNatureOfInformationMapper.toDto(crbNatureOfInformationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbNatureOfInformationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbNatureOfInformationDTO> findByCriteria(CrbNatureOfInformationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbNatureOfInformation> specification = createSpecification(criteria);
        return crbNatureOfInformationRepository.findAll(specification, page).map(crbNatureOfInformationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbNatureOfInformationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbNatureOfInformation> specification = createSpecification(criteria);
        return crbNatureOfInformationRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbNatureOfInformationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbNatureOfInformation> createSpecification(CrbNatureOfInformationCriteria criteria) {
        Specification<CrbNatureOfInformation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbNatureOfInformation_.id));
            }
            if (criteria.getNatureOfInformationTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getNatureOfInformationTypeCode(),
                            CrbNatureOfInformation_.natureOfInformationTypeCode
                        )
                    );
            }
            if (criteria.getNatureOfInformationType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getNatureOfInformationType(), CrbNatureOfInformation_.natureOfInformationType)
                    );
            }
        }
        return specification;
    }
}
