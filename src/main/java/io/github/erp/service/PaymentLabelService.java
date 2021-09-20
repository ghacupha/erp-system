package io.github.erp.service;

import io.github.erp.service.dto.PaymentLabelDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PaymentLabel}.
 */
public interface PaymentLabelService {
    /**
     * Save a paymentLabel.
     *
     * @param paymentLabelDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentLabelDTO save(PaymentLabelDTO paymentLabelDTO);

    /**
     * Partially updates a paymentLabel.
     *
     * @param paymentLabelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentLabelDTO> partialUpdate(PaymentLabelDTO paymentLabelDTO);

    /**
     * Get all the paymentLabels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentLabelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paymentLabel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentLabelDTO> findOne(Long id);

    /**
     * Delete the "id" paymentLabel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the paymentLabel corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentLabelDTO> search(String query, Pageable pageable);
}
