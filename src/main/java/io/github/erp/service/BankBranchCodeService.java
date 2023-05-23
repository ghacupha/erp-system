package io.github.erp.service;

import io.github.erp.service.dto.BankBranchCodeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.BankBranchCode}.
 */
public interface BankBranchCodeService {
    /**
     * Save a bankBranchCode.
     *
     * @param bankBranchCodeDTO the entity to save.
     * @return the persisted entity.
     */
    BankBranchCodeDTO save(BankBranchCodeDTO bankBranchCodeDTO);

    /**
     * Partially updates a bankBranchCode.
     *
     * @param bankBranchCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BankBranchCodeDTO> partialUpdate(BankBranchCodeDTO bankBranchCodeDTO);

    /**
     * Get all the bankBranchCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BankBranchCodeDTO> findAll(Pageable pageable);

    /**
     * Get all the bankBranchCodes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BankBranchCodeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" bankBranchCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankBranchCodeDTO> findOne(Long id);

    /**
     * Delete the "id" bankBranchCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the bankBranchCode corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BankBranchCodeDTO> search(String query, Pageable pageable);
}
