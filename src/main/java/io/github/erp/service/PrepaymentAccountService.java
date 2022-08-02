package io.github.erp.service;

import io.github.erp.service.dto.PrepaymentAccountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PrepaymentAccount}.
 */
public interface PrepaymentAccountService {
    /**
     * Save a prepaymentAccount.
     *
     * @param prepaymentAccountDTO the entity to save.
     * @return the persisted entity.
     */
    PrepaymentAccountDTO save(PrepaymentAccountDTO prepaymentAccountDTO);

    /**
     * Partially updates a prepaymentAccount.
     *
     * @param prepaymentAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrepaymentAccountDTO> partialUpdate(PrepaymentAccountDTO prepaymentAccountDTO);

    /**
     * Get all the prepaymentAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentAccountDTO> findAll(Pageable pageable);

    /**
     * Get all the prepaymentAccounts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentAccountDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" prepaymentAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrepaymentAccountDTO> findOne(Long id);

    /**
     * Delete the "id" prepaymentAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the prepaymentAccount corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentAccountDTO> search(String query, Pageable pageable);
}
