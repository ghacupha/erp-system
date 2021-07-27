package io.github.erp.service;

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

import io.github.erp.domain.FixedAssetNetBookValue;
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.repository.FixedAssetNetBookValueRepository;
import io.github.erp.repository.search.FixedAssetNetBookValueSearchRepository;
import io.github.erp.service.dto.FixedAssetNetBookValueCriteria;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import io.github.erp.service.mapper.FixedAssetNetBookValueMapper;

/**
 * Service for executing complex queries for {@link FixedAssetNetBookValue} entities in the database.
 * The main input is a {@link FixedAssetNetBookValueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FixedAssetNetBookValueDTO} or a {@link Page} of {@link FixedAssetNetBookValueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FixedAssetNetBookValueQueryService extends QueryService<FixedAssetNetBookValue> {

    private final Logger log = LoggerFactory.getLogger(FixedAssetNetBookValueQueryService.class);

    private final FixedAssetNetBookValueRepository fixedAssetNetBookValueRepository;

    private final FixedAssetNetBookValueMapper fixedAssetNetBookValueMapper;

    private final FixedAssetNetBookValueSearchRepository fixedAssetNetBookValueSearchRepository;

    public FixedAssetNetBookValueQueryService(FixedAssetNetBookValueRepository fixedAssetNetBookValueRepository, FixedAssetNetBookValueMapper fixedAssetNetBookValueMapper, FixedAssetNetBookValueSearchRepository fixedAssetNetBookValueSearchRepository) {
        this.fixedAssetNetBookValueRepository = fixedAssetNetBookValueRepository;
        this.fixedAssetNetBookValueMapper = fixedAssetNetBookValueMapper;
        this.fixedAssetNetBookValueSearchRepository = fixedAssetNetBookValueSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FixedAssetNetBookValueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FixedAssetNetBookValueDTO> findByCriteria(FixedAssetNetBookValueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FixedAssetNetBookValue> specification = createSpecification(criteria);
        return fixedAssetNetBookValueMapper.toDto(fixedAssetNetBookValueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FixedAssetNetBookValueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FixedAssetNetBookValueDTO> findByCriteria(FixedAssetNetBookValueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FixedAssetNetBookValue> specification = createSpecification(criteria);
        return fixedAssetNetBookValueRepository.findAll(specification, page)
            .map(fixedAssetNetBookValueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FixedAssetNetBookValueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FixedAssetNetBookValue> specification = createSpecification(criteria);
        return fixedAssetNetBookValueRepository.count(specification);
    }

    /**
     * Function to convert {@link FixedAssetNetBookValueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FixedAssetNetBookValue> createSpecification(FixedAssetNetBookValueCriteria criteria) {
        Specification<FixedAssetNetBookValue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FixedAssetNetBookValue_.id));
            }
            if (criteria.getAssetNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetNumber(), FixedAssetNetBookValue_.assetNumber));
            }
            if (criteria.getServiceOutletCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceOutletCode(), FixedAssetNetBookValue_.serviceOutletCode));
            }
            if (criteria.getAssetTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetTag(), FixedAssetNetBookValue_.assetTag));
            }
            if (criteria.getAssetDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetDescription(), FixedAssetNetBookValue_.assetDescription));
            }
            if (criteria.getNetBookValueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNetBookValueDate(), FixedAssetNetBookValue_.netBookValueDate));
            }
            if (criteria.getAssetCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetCategory(), FixedAssetNetBookValue_.assetCategory));
            }
            if (criteria.getNetBookValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNetBookValue(), FixedAssetNetBookValue_.netBookValue));
            }
            if (criteria.getDepreciationRegime() != null) {
                specification = specification.and(buildSpecification(criteria.getDepreciationRegime(), FixedAssetNetBookValue_.depreciationRegime));
            }
            if (criteria.getFileUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUploadToken(), FixedAssetNetBookValue_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompilationToken(), FixedAssetNetBookValue_.compilationToken));
            }
        }
        return specification;
    }
}
