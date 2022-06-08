package io.github.erp.service;

import io.github.erp.service.dto.SubCountyCodeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SubCountyCode}.
 */
public interface SubCountyCodeService {
    /**
     * Save a subCountyCode.
     *
     * @param subCountyCodeDTO the entity to save.
     * @return the persisted entity.
     */
    SubCountyCodeDTO save(SubCountyCodeDTO subCountyCodeDTO);

    /**
     * Partially updates a subCountyCode.
     *
     * @param subCountyCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubCountyCodeDTO> partialUpdate(SubCountyCodeDTO subCountyCodeDTO);

    /**
     * Get all the subCountyCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubCountyCodeDTO> findAll(Pageable pageable);

    /**
     * Get all the subCountyCodes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubCountyCodeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" subCountyCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubCountyCodeDTO> findOne(Long id);

    /**
     * Delete the "id" subCountyCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the subCountyCode corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubCountyCodeDTO> search(String query, Pageable pageable);
}
