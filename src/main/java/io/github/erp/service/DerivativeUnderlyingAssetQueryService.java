package io.github.erp.service;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
import io.github.erp.domain.DerivativeUnderlyingAsset;
import io.github.erp.repository.DerivativeUnderlyingAssetRepository;
import io.github.erp.repository.search.DerivativeUnderlyingAssetSearchRepository;
import io.github.erp.service.criteria.DerivativeUnderlyingAssetCriteria;
import io.github.erp.service.dto.DerivativeUnderlyingAssetDTO;
import io.github.erp.service.mapper.DerivativeUnderlyingAssetMapper;
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
 * Service for executing complex queries for {@link DerivativeUnderlyingAsset} entities in the database.
 * The main input is a {@link DerivativeUnderlyingAssetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DerivativeUnderlyingAssetDTO} or a {@link Page} of {@link DerivativeUnderlyingAssetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DerivativeUnderlyingAssetQueryService extends QueryService<DerivativeUnderlyingAsset> {

    private final Logger log = LoggerFactory.getLogger(DerivativeUnderlyingAssetQueryService.class);

    private final DerivativeUnderlyingAssetRepository derivativeUnderlyingAssetRepository;

    private final DerivativeUnderlyingAssetMapper derivativeUnderlyingAssetMapper;

    private final DerivativeUnderlyingAssetSearchRepository derivativeUnderlyingAssetSearchRepository;

    public DerivativeUnderlyingAssetQueryService(
        DerivativeUnderlyingAssetRepository derivativeUnderlyingAssetRepository,
        DerivativeUnderlyingAssetMapper derivativeUnderlyingAssetMapper,
        DerivativeUnderlyingAssetSearchRepository derivativeUnderlyingAssetSearchRepository
    ) {
        this.derivativeUnderlyingAssetRepository = derivativeUnderlyingAssetRepository;
        this.derivativeUnderlyingAssetMapper = derivativeUnderlyingAssetMapper;
        this.derivativeUnderlyingAssetSearchRepository = derivativeUnderlyingAssetSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DerivativeUnderlyingAssetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DerivativeUnderlyingAssetDTO> findByCriteria(DerivativeUnderlyingAssetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DerivativeUnderlyingAsset> specification = createSpecification(criteria);
        return derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAssetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DerivativeUnderlyingAssetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DerivativeUnderlyingAssetDTO> findByCriteria(DerivativeUnderlyingAssetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DerivativeUnderlyingAsset> specification = createSpecification(criteria);
        return derivativeUnderlyingAssetRepository.findAll(specification, page).map(derivativeUnderlyingAssetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DerivativeUnderlyingAssetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DerivativeUnderlyingAsset> specification = createSpecification(criteria);
        return derivativeUnderlyingAssetRepository.count(specification);
    }

    /**
     * Function to convert {@link DerivativeUnderlyingAssetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DerivativeUnderlyingAsset> createSpecification(DerivativeUnderlyingAssetCriteria criteria) {
        Specification<DerivativeUnderlyingAsset> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DerivativeUnderlyingAsset_.id));
            }
            if (criteria.getDerivativeUnderlyingAssetTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getDerivativeUnderlyingAssetTypeCode(),
                            DerivativeUnderlyingAsset_.derivativeUnderlyingAssetTypeCode
                        )
                    );
            }
            if (criteria.getFinancialDerivativeUnderlyingAssetType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getFinancialDerivativeUnderlyingAssetType(),
                            DerivativeUnderlyingAsset_.financialDerivativeUnderlyingAssetType
                        )
                    );
            }
        }
        return specification;
    }
}
