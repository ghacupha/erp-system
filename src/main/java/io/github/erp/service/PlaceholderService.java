package io.github.erp.service;

import io.github.erp.service.dto.PlaceholderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.Placeholder}.
 */
public interface PlaceholderService {
    /**
     * Save a placeholder.
     *
     * @param placeholderDTO the entity to save.
     * @return the persisted entity.
     */
    PlaceholderDTO save(PlaceholderDTO placeholderDTO);

    /**
     * Partially updates a placeholder.
     *
     * @param placeholderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlaceholderDTO> partialUpdate(PlaceholderDTO placeholderDTO);

    /**
     * Get all the placeholders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlaceholderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" placeholder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlaceholderDTO> findOne(Long id);

    /**
     * Delete the "id" placeholder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the placeholder corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlaceholderDTO> search(String query, Pageable pageable);
}
