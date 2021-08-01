package io.github.erp.service;

import io.github.erp.service.dto.MessageTokenDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.MessageToken}.
 */
public interface MessageTokenService {
    /**
     * Save a messageToken.
     *
     * @param messageTokenDTO the entity to save.
     * @return the persisted entity.
     */
    MessageTokenDTO save(MessageTokenDTO messageTokenDTO);

    /**
     * Partially updates a messageToken.
     *
     * @param messageTokenDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MessageTokenDTO> partialUpdate(MessageTokenDTO messageTokenDTO);

    /**
     * Get all the messageTokens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MessageTokenDTO> findAll(Pageable pageable);

    /**
     * Get the "id" messageToken.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MessageTokenDTO> findOne(Long id);

    /**
     * Delete the "id" messageToken.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the messageToken corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MessageTokenDTO> search(String query, Pageable pageable);
}
