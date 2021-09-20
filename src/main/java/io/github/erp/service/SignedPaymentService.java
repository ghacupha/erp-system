package io.github.erp.service;

import io.github.erp.service.dto.SignedPaymentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SignedPayment}.
 */
public interface SignedPaymentService {
    /**
     * Save a signedPayment.
     *
     * @param signedPaymentDTO the entity to save.
     * @return the persisted entity.
     */
    SignedPaymentDTO save(SignedPaymentDTO signedPaymentDTO);

    /**
     * Partially updates a signedPayment.
     *
     * @param signedPaymentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SignedPaymentDTO> partialUpdate(SignedPaymentDTO signedPaymentDTO);

    /**
     * Get all the signedPayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SignedPaymentDTO> findAll(Pageable pageable);

    /**
     * Get all the signedPayments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SignedPaymentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" signedPayment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SignedPaymentDTO> findOne(Long id);

    /**
     * Delete the "id" signedPayment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the signedPayment corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SignedPaymentDTO> search(String query, Pageable pageable);
}
