package io.github.erp.service;

import io.github.erp.service.dto.CountyCodeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.CountyCode}.
 */
public interface CountyCodeService {
    /**
     * Save a countyCode.
     *
     * @param countyCodeDTO the entity to save.
     * @return the persisted entity.
     */
    CountyCodeDTO save(CountyCodeDTO countyCodeDTO);

    /**
     * Partially updates a countyCode.
     *
     * @param countyCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CountyCodeDTO> partialUpdate(CountyCodeDTO countyCodeDTO);

    /**
     * Get all the countyCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CountyCodeDTO> findAll(Pageable pageable);

    /**
     * Get all the countyCodes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CountyCodeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" countyCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CountyCodeDTO> findOne(Long id);

    /**
     * Delete the "id" countyCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the countyCode corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CountyCodeDTO> search(String query, Pageable pageable);
}
