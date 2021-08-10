package io.github.erp.service;

import io.github.erp.service.dto.TaxRuleDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.TaxRule}.
 */
public interface TaxRuleService {
    /**
     * Save a taxRule.
     *
     * @param taxRuleDTO the entity to save.
     * @return the persisted entity.
     */
    TaxRuleDTO save(TaxRuleDTO taxRuleDTO);

    /**
     * Partially updates a taxRule.
     *
     * @param taxRuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaxRuleDTO> partialUpdate(TaxRuleDTO taxRuleDTO);

    /**
     * Get all the taxRules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxRuleDTO> findAll(Pageable pageable);
    /**
     * Get all the TaxRuleDTO where Payment is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<TaxRuleDTO> findAllWherePaymentIsNull();

    /**
     * Get the "id" taxRule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaxRuleDTO> findOne(Long id);

    /**
     * Delete the "id" taxRule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the taxRule corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxRuleDTO> search(String query, Pageable pageable);
}
