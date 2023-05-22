package io.github.erp.service;

import io.github.erp.service.dto.SettlementCurrencyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SettlementCurrency}.
 */
public interface SettlementCurrencyService {
    /**
     * Save a settlementCurrency.
     *
     * @param settlementCurrencyDTO the entity to save.
     * @return the persisted entity.
     */
    SettlementCurrencyDTO save(SettlementCurrencyDTO settlementCurrencyDTO);

    /**
     * Partially updates a settlementCurrency.
     *
     * @param settlementCurrencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SettlementCurrencyDTO> partialUpdate(SettlementCurrencyDTO settlementCurrencyDTO);

    /**
     * Get all the settlementCurrencies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementCurrencyDTO> findAll(Pageable pageable);

    /**
     * Get all the settlementCurrencies with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementCurrencyDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" settlementCurrency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SettlementCurrencyDTO> findOne(Long id);

    /**
     * Delete the "id" settlementCurrency.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the settlementCurrency corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementCurrencyDTO> search(String query, Pageable pageable);
}
