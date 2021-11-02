package io.github.erp.service;

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.PlaceholderRepository;
import io.github.erp.repository.search.PlaceholderSearchRepository;
import io.github.erp.service.criteria.PlaceholderCriteria;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.mapper.PlaceholderMapper;
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
 * Service for executing complex queries for {@link Placeholder} entities in the database.
 * The main input is a {@link PlaceholderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlaceholderDTO} or a {@link Page} of {@link PlaceholderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlaceholderQueryService extends QueryService<Placeholder> {

    private final Logger log = LoggerFactory.getLogger(PlaceholderQueryService.class);

    private final PlaceholderRepository placeholderRepository;

    private final PlaceholderMapper placeholderMapper;

    private final PlaceholderSearchRepository placeholderSearchRepository;

    public PlaceholderQueryService(
        PlaceholderRepository placeholderRepository,
        PlaceholderMapper placeholderMapper,
        PlaceholderSearchRepository placeholderSearchRepository
    ) {
        this.placeholderRepository = placeholderRepository;
        this.placeholderMapper = placeholderMapper;
        this.placeholderSearchRepository = placeholderSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PlaceholderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlaceholderDTO> findByCriteria(PlaceholderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Placeholder> specification = createSpecification(criteria);
        return placeholderMapper.toDto(placeholderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlaceholderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlaceholderDTO> findByCriteria(PlaceholderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Placeholder> specification = createSpecification(criteria);
        return placeholderRepository.findAll(specification, page).map(placeholderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlaceholderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Placeholder> specification = createSpecification(criteria);
        return placeholderRepository.count(specification);
    }

    /**
     * Function to convert {@link PlaceholderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Placeholder> createSpecification(PlaceholderCriteria criteria) {
        Specification<Placeholder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Placeholder_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Placeholder_.description));
            }
            if (criteria.getToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getToken(), Placeholder_.token));
            }
            if (criteria.getFileUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUploadToken(), Placeholder_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompilationToken(), Placeholder_.compilationToken));
            }
            if (criteria.getContainingPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContainingPlaceholderId(),
                            root -> root.join(Placeholder_.containingPlaceholder, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}