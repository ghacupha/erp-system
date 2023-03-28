package io.github.erp.service;

import io.github.erp.service.dto.QuestionBaseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.QuestionBase}.
 */
public interface QuestionBaseService {
    /**
     * Save a questionBase.
     *
     * @param questionBaseDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionBaseDTO save(QuestionBaseDTO questionBaseDTO);

    /**
     * Partially updates a questionBase.
     *
     * @param questionBaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionBaseDTO> partialUpdate(QuestionBaseDTO questionBaseDTO);

    /**
     * Get all the questionBases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionBaseDTO> findAll(Pageable pageable);

    /**
     * Get all the questionBases with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionBaseDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" questionBase.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionBaseDTO> findOne(Long id);

    /**
     * Delete the "id" questionBase.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the questionBase corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionBaseDTO> search(String query, Pageable pageable);
}
