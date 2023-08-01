package io.github.erp.service;

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.repository.DepreciationPeriodRepository;
import io.github.erp.repository.search.DepreciationPeriodSearchRepository;
import io.github.erp.service.criteria.DepreciationPeriodCriteria;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.mapper.DepreciationPeriodMapper;
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
 * Service for executing complex queries for {@link DepreciationPeriod} entities in the database.
 * The main input is a {@link DepreciationPeriodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepreciationPeriodDTO} or a {@link Page} of {@link DepreciationPeriodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepreciationPeriodQueryService extends QueryService<DepreciationPeriod> {

    private final Logger log = LoggerFactory.getLogger(DepreciationPeriodQueryService.class);

    private final DepreciationPeriodRepository depreciationPeriodRepository;

    private final DepreciationPeriodMapper depreciationPeriodMapper;

    private final DepreciationPeriodSearchRepository depreciationPeriodSearchRepository;

    public DepreciationPeriodQueryService(
        DepreciationPeriodRepository depreciationPeriodRepository,
        DepreciationPeriodMapper depreciationPeriodMapper,
        DepreciationPeriodSearchRepository depreciationPeriodSearchRepository
    ) {
        this.depreciationPeriodRepository = depreciationPeriodRepository;
        this.depreciationPeriodMapper = depreciationPeriodMapper;
        this.depreciationPeriodSearchRepository = depreciationPeriodSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DepreciationPeriodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepreciationPeriodDTO> findByCriteria(DepreciationPeriodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DepreciationPeriod> specification = createSpecification(criteria);
        return depreciationPeriodMapper.toDto(depreciationPeriodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepreciationPeriodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepreciationPeriodDTO> findByCriteria(DepreciationPeriodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepreciationPeriod> specification = createSpecification(criteria);
        return depreciationPeriodRepository.findAll(specification, page).map(depreciationPeriodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepreciationPeriodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DepreciationPeriod> specification = createSpecification(criteria);
        return depreciationPeriodRepository.count(specification);
    }

    /**
     * Function to convert {@link DepreciationPeriodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepreciationPeriod> createSpecification(DepreciationPeriodCriteria criteria) {
        Specification<DepreciationPeriod> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DepreciationPeriod_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), DepreciationPeriod_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), DepreciationPeriod_.endDate));
            }
            if (criteria.getDepreciationPeriodStatus() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDepreciationPeriodStatus(), DepreciationPeriod_.depreciationPeriodStatus)
                    );
            }
            if (criteria.getPreviousPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPreviousPeriodId(),
                            root -> root.join(DepreciationPeriod_.previousPeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
