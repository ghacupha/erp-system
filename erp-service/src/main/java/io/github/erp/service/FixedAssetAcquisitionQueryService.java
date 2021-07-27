package io.github.erp.service;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.erp.domain.FixedAssetAcquisition;
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.repository.FixedAssetAcquisitionRepository;
import io.github.erp.repository.search.FixedAssetAcquisitionSearchRepository;
import io.github.erp.service.dto.FixedAssetAcquisitionCriteria;
import io.github.erp.service.dto.FixedAssetAcquisitionDTO;
import io.github.erp.service.mapper.FixedAssetAcquisitionMapper;

/**
 * Service for executing complex queries for {@link FixedAssetAcquisition} entities in the database.
 * The main input is a {@link FixedAssetAcquisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FixedAssetAcquisitionDTO} or a {@link Page} of {@link FixedAssetAcquisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FixedAssetAcquisitionQueryService extends QueryService<FixedAssetAcquisition> {

    private final Logger log = LoggerFactory.getLogger(FixedAssetAcquisitionQueryService.class);

    private final FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository;

    private final FixedAssetAcquisitionMapper fixedAssetAcquisitionMapper;

    private final FixedAssetAcquisitionSearchRepository fixedAssetAcquisitionSearchRepository;

    public FixedAssetAcquisitionQueryService(FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository, FixedAssetAcquisitionMapper fixedAssetAcquisitionMapper, FixedAssetAcquisitionSearchRepository fixedAssetAcquisitionSearchRepository) {
        this.fixedAssetAcquisitionRepository = fixedAssetAcquisitionRepository;
        this.fixedAssetAcquisitionMapper = fixedAssetAcquisitionMapper;
        this.fixedAssetAcquisitionSearchRepository = fixedAssetAcquisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FixedAssetAcquisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FixedAssetAcquisitionDTO> findByCriteria(FixedAssetAcquisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FixedAssetAcquisition> specification = createSpecification(criteria);
        return fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FixedAssetAcquisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FixedAssetAcquisitionDTO> findByCriteria(FixedAssetAcquisitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FixedAssetAcquisition> specification = createSpecification(criteria);
        return fixedAssetAcquisitionRepository.findAll(specification, page)
            .map(fixedAssetAcquisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FixedAssetAcquisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FixedAssetAcquisition> specification = createSpecification(criteria);
        return fixedAssetAcquisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link FixedAssetAcquisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FixedAssetAcquisition> createSpecification(FixedAssetAcquisitionCriteria criteria) {
        Specification<FixedAssetAcquisition> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FixedAssetAcquisition_.id));
            }
            if (criteria.getAssetNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetNumber(), FixedAssetAcquisition_.assetNumber));
            }
            if (criteria.getServiceOutletCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceOutletCode(), FixedAssetAcquisition_.serviceOutletCode));
            }
            if (criteria.getAssetTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetTag(), FixedAssetAcquisition_.assetTag));
            }
            if (criteria.getAssetDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetDescription(), FixedAssetAcquisition_.assetDescription));
            }
            if (criteria.getPurchaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPurchaseDate(), FixedAssetAcquisition_.purchaseDate));
            }
            if (criteria.getAssetCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetCategory(), FixedAssetAcquisition_.assetCategory));
            }
            if (criteria.getPurchasePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPurchasePrice(), FixedAssetAcquisition_.purchasePrice));
            }
            if (criteria.getFileUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUploadToken(), FixedAssetAcquisition_.fileUploadToken));
            }
        }
        return specification;
    }
}
