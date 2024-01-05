package io.github.erp.service;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.MerchantType;
import io.github.erp.repository.MerchantTypeRepository;
import io.github.erp.repository.search.MerchantTypeSearchRepository;
import io.github.erp.service.criteria.MerchantTypeCriteria;
import io.github.erp.service.dto.MerchantTypeDTO;
import io.github.erp.service.mapper.MerchantTypeMapper;
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
 * Service for executing complex queries for {@link MerchantType} entities in the database.
 * The main input is a {@link MerchantTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MerchantTypeDTO} or a {@link Page} of {@link MerchantTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MerchantTypeQueryService extends QueryService<MerchantType> {

    private final Logger log = LoggerFactory.getLogger(MerchantTypeQueryService.class);

    private final MerchantTypeRepository merchantTypeRepository;

    private final MerchantTypeMapper merchantTypeMapper;

    private final MerchantTypeSearchRepository merchantTypeSearchRepository;

    public MerchantTypeQueryService(
        MerchantTypeRepository merchantTypeRepository,
        MerchantTypeMapper merchantTypeMapper,
        MerchantTypeSearchRepository merchantTypeSearchRepository
    ) {
        this.merchantTypeRepository = merchantTypeRepository;
        this.merchantTypeMapper = merchantTypeMapper;
        this.merchantTypeSearchRepository = merchantTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MerchantTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MerchantTypeDTO> findByCriteria(MerchantTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MerchantType> specification = createSpecification(criteria);
        return merchantTypeMapper.toDto(merchantTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MerchantTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MerchantTypeDTO> findByCriteria(MerchantTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MerchantType> specification = createSpecification(criteria);
        return merchantTypeRepository.findAll(specification, page).map(merchantTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MerchantTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MerchantType> specification = createSpecification(criteria);
        return merchantTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link MerchantTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MerchantType> createSpecification(MerchantTypeCriteria criteria) {
        Specification<MerchantType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MerchantType_.id));
            }
            if (criteria.getMerchantTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMerchantTypeCode(), MerchantType_.merchantTypeCode));
            }
            if (criteria.getMerchantType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMerchantType(), MerchantType_.merchantType));
            }
        }
        return specification;
    }
}
