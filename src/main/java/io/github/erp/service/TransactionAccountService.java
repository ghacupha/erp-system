package io.github.erp.service;

import io.github.erp.service.dto.TransactionAccountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.TransactionAccount}.
 */
public interface TransactionAccountService {
    /**
     * Save a transactionAccount.
     *
     * @param transactionAccountDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionAccountDTO save(TransactionAccountDTO transactionAccountDTO);

    /**
     * Partially updates a transactionAccount.
     *
     * @param transactionAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionAccountDTO> partialUpdate(TransactionAccountDTO transactionAccountDTO);

    /**
     * Get all the transactionAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionAccountDTO> findAll(Pageable pageable);

    /**
     * Get all the transactionAccounts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionAccountDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" transactionAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionAccountDTO> findOne(Long id);

    /**
     * Delete the "id" transactionAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the transactionAccount corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionAccountDTO> search(String query, Pageable pageable);
}
