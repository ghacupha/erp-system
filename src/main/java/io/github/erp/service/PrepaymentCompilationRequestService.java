package io.github.erp.service;

import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PrepaymentCompilationRequest}.
 */
public interface PrepaymentCompilationRequestService {
    /**
     * Save a prepaymentCompilationRequest.
     *
     * @param prepaymentCompilationRequestDTO the entity to save.
     * @return the persisted entity.
     */
    PrepaymentCompilationRequestDTO save(PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO);

    /**
     * Partially updates a prepaymentCompilationRequest.
     *
     * @param prepaymentCompilationRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrepaymentCompilationRequestDTO> partialUpdate(PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO);

    /**
     * Get all the prepaymentCompilationRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentCompilationRequestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" prepaymentCompilationRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrepaymentCompilationRequestDTO> findOne(Long id);

    /**
     * Delete the "id" prepaymentCompilationRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the prepaymentCompilationRequest corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentCompilationRequestDTO> search(String query, Pageable pageable);
}
