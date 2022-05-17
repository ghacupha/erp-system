package io.github.erp.service;

import io.github.erp.service.dto.InstitutionCodeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.InstitutionCode}.
 */
public interface InstitutionCodeService {
    /**
     * Save a institutionCode.
     *
     * @param institutionCodeDTO the entity to save.
     * @return the persisted entity.
     */
    InstitutionCodeDTO save(InstitutionCodeDTO institutionCodeDTO);

    /**
     * Partially updates a institutionCode.
     *
     * @param institutionCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InstitutionCodeDTO> partialUpdate(InstitutionCodeDTO institutionCodeDTO);

    /**
     * Get all the institutionCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InstitutionCodeDTO> findAll(Pageable pageable);

    /**
     * Get all the institutionCodes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InstitutionCodeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" institutionCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InstitutionCodeDTO> findOne(Long id);

    /**
     * Delete the "id" institutionCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the institutionCode corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InstitutionCodeDTO> search(String query, Pageable pageable);
}
