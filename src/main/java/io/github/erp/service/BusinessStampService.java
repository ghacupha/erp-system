package io.github.erp.service;

import io.github.erp.service.dto.BusinessStampDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.BusinessStamp}.
 */
public interface BusinessStampService {
    /**
     * Save a businessStamp.
     *
     * @param businessStampDTO the entity to save.
     * @return the persisted entity.
     */
    BusinessStampDTO save(BusinessStampDTO businessStampDTO);

    /**
     * Partially updates a businessStamp.
     *
     * @param businessStampDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusinessStampDTO> partialUpdate(BusinessStampDTO businessStampDTO);

    /**
     * Get all the businessStamps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessStampDTO> findAll(Pageable pageable);

    /**
     * Get all the businessStamps with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessStampDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" businessStamp.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusinessStampDTO> findOne(Long id);

    /**
     * Delete the "id" businessStamp.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the businessStamp corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessStampDTO> search(String query, Pageable pageable);
}
