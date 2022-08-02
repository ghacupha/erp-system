package io.github.erp.service;

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.AmortizationSequence;
import io.github.erp.repository.AmortizationSequenceRepository;
import io.github.erp.repository.search.AmortizationSequenceSearchRepository;
import io.github.erp.service.criteria.AmortizationSequenceCriteria;
import io.github.erp.service.dto.AmortizationSequenceDTO;
import io.github.erp.service.mapper.AmortizationSequenceMapper;
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
 * Service for executing complex queries for {@link AmortizationSequence} entities in the database.
 * The main input is a {@link AmortizationSequenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AmortizationSequenceDTO} or a {@link Page} of {@link AmortizationSequenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AmortizationSequenceQueryService extends QueryService<AmortizationSequence> {

    private final Logger log = LoggerFactory.getLogger(AmortizationSequenceQueryService.class);

    private final AmortizationSequenceRepository amortizationSequenceRepository;

    private final AmortizationSequenceMapper amortizationSequenceMapper;

    private final AmortizationSequenceSearchRepository amortizationSequenceSearchRepository;

    public AmortizationSequenceQueryService(
        AmortizationSequenceRepository amortizationSequenceRepository,
        AmortizationSequenceMapper amortizationSequenceMapper,
        AmortizationSequenceSearchRepository amortizationSequenceSearchRepository
    ) {
        this.amortizationSequenceRepository = amortizationSequenceRepository;
        this.amortizationSequenceMapper = amortizationSequenceMapper;
        this.amortizationSequenceSearchRepository = amortizationSequenceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AmortizationSequenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AmortizationSequenceDTO> findByCriteria(AmortizationSequenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AmortizationSequence> specification = createSpecification(criteria);
        return amortizationSequenceMapper.toDto(amortizationSequenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AmortizationSequenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AmortizationSequenceDTO> findByCriteria(AmortizationSequenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AmortizationSequence> specification = createSpecification(criteria);
        return amortizationSequenceRepository.findAll(specification, page).map(amortizationSequenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AmortizationSequenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AmortizationSequence> specification = createSpecification(criteria);
        return amortizationSequenceRepository.count(specification);
    }

    /**
     * Function to convert {@link AmortizationSequenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AmortizationSequence> createSpecification(AmortizationSequenceCriteria criteria) {
        Specification<AmortizationSequence> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AmortizationSequence_.id));
            }
            if (criteria.getPrepaymentAccountGuid() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getPrepaymentAccountGuid(), AmortizationSequence_.prepaymentAccountGuid));
            }
            if (criteria.getRecurrenceGuid() != null) {
                specification = specification.and(buildSpecification(criteria.getRecurrenceGuid(), AmortizationSequence_.recurrenceGuid));
            }
            if (criteria.getSequenceNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSequenceNumber(), AmortizationSequence_.sequenceNumber));
            }
            if (criteria.getParticulars() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParticulars(), AmortizationSequence_.particulars));
            }
            if (criteria.getCurrentAmortizationDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getCurrentAmortizationDate(), AmortizationSequence_.currentAmortizationDate)
                    );
            }
            if (criteria.getPreviousAmortizationDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getPreviousAmortizationDate(), AmortizationSequence_.previousAmortizationDate)
                    );
            }
            if (criteria.getNextAmortizationDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNextAmortizationDate(), AmortizationSequence_.nextAmortizationDate)
                    );
            }
            if (criteria.getIsCommencementSequence() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getIsCommencementSequence(), AmortizationSequence_.isCommencementSequence)
                    );
            }
            if (criteria.getIsTerminalSequence() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getIsTerminalSequence(), AmortizationSequence_.isTerminalSequence));
            }
            if (criteria.getAmortizationAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAmortizationAmount(), AmortizationSequence_.amortizationAmount));
            }
            if (criteria.getSequenceGuid() != null) {
                specification = specification.and(buildSpecification(criteria.getSequenceGuid(), AmortizationSequence_.sequenceGuid));
            }
            if (criteria.getPrepaymentAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrepaymentAccountId(),
                            root -> root.join(AmortizationSequence_.prepaymentAccount, JoinType.LEFT).get(PrepaymentAccount_.id)
                        )
                    );
            }
            if (criteria.getAmortizationRecurrenceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAmortizationRecurrenceId(),
                            root -> root.join(AmortizationSequence_.amortizationRecurrence, JoinType.LEFT).get(AmortizationRecurrence_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AmortizationSequence_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getPrepaymentMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrepaymentMappingId(),
                            root -> root.join(AmortizationSequence_.prepaymentMappings, JoinType.LEFT).get(PrepaymentMapping_.id)
                        )
                    );
            }
            if (criteria.getApplicationParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApplicationParametersId(),
                            root -> root.join(AmortizationSequence_.applicationParameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
