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
import io.github.erp.domain.UltimateBeneficiaryCategory;
import io.github.erp.repository.UltimateBeneficiaryCategoryRepository;
import io.github.erp.repository.search.UltimateBeneficiaryCategorySearchRepository;
import io.github.erp.service.criteria.UltimateBeneficiaryCategoryCriteria;
import io.github.erp.service.dto.UltimateBeneficiaryCategoryDTO;
import io.github.erp.service.mapper.UltimateBeneficiaryCategoryMapper;
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
 * Service for executing complex queries for {@link UltimateBeneficiaryCategory} entities in the database.
 * The main input is a {@link UltimateBeneficiaryCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UltimateBeneficiaryCategoryDTO} or a {@link Page} of {@link UltimateBeneficiaryCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UltimateBeneficiaryCategoryQueryService extends QueryService<UltimateBeneficiaryCategory> {

    private final Logger log = LoggerFactory.getLogger(UltimateBeneficiaryCategoryQueryService.class);

    private final UltimateBeneficiaryCategoryRepository ultimateBeneficiaryCategoryRepository;

    private final UltimateBeneficiaryCategoryMapper ultimateBeneficiaryCategoryMapper;

    private final UltimateBeneficiaryCategorySearchRepository ultimateBeneficiaryCategorySearchRepository;

    public UltimateBeneficiaryCategoryQueryService(
        UltimateBeneficiaryCategoryRepository ultimateBeneficiaryCategoryRepository,
        UltimateBeneficiaryCategoryMapper ultimateBeneficiaryCategoryMapper,
        UltimateBeneficiaryCategorySearchRepository ultimateBeneficiaryCategorySearchRepository
    ) {
        this.ultimateBeneficiaryCategoryRepository = ultimateBeneficiaryCategoryRepository;
        this.ultimateBeneficiaryCategoryMapper = ultimateBeneficiaryCategoryMapper;
        this.ultimateBeneficiaryCategorySearchRepository = ultimateBeneficiaryCategorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link UltimateBeneficiaryCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UltimateBeneficiaryCategoryDTO> findByCriteria(UltimateBeneficiaryCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UltimateBeneficiaryCategory> specification = createSpecification(criteria);
        return ultimateBeneficiaryCategoryMapper.toDto(ultimateBeneficiaryCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UltimateBeneficiaryCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UltimateBeneficiaryCategoryDTO> findByCriteria(UltimateBeneficiaryCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UltimateBeneficiaryCategory> specification = createSpecification(criteria);
        return ultimateBeneficiaryCategoryRepository.findAll(specification, page).map(ultimateBeneficiaryCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UltimateBeneficiaryCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UltimateBeneficiaryCategory> specification = createSpecification(criteria);
        return ultimateBeneficiaryCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link UltimateBeneficiaryCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UltimateBeneficiaryCategory> createSpecification(UltimateBeneficiaryCategoryCriteria criteria) {
        Specification<UltimateBeneficiaryCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UltimateBeneficiaryCategory_.id));
            }
            if (criteria.getUltimateBeneficiaryCategoryTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getUltimateBeneficiaryCategoryTypeCode(),
                            UltimateBeneficiaryCategory_.ultimateBeneficiaryCategoryTypeCode
                        )
                    );
            }
            if (criteria.getUltimateBeneficiaryType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getUltimateBeneficiaryType(),
                            UltimateBeneficiaryCategory_.ultimateBeneficiaryType
                        )
                    );
            }
        }
        return specification;
    }
}
