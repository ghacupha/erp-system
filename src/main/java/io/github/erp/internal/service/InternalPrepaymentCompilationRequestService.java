package io.github.erp.internal.service;

import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InternalPrepaymentCompilationRequestService {

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
