package io.github.erp.service;

import io.github.erp.service.dto.PaymentCalculationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PaymentCalculation}.
 */
public interface PaymentCalculationService {
    /**
     * Save a paymentCalculation.
     *
     * @param paymentCalculationDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentCalculationDTO save(PaymentCalculationDTO paymentCalculationDTO);

    /**
     * Partially updates a paymentCalculation.
     *
     * @param paymentCalculationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentCalculationDTO> partialUpdate(PaymentCalculationDTO paymentCalculationDTO);

    /**
     * Get all the paymentCalculations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentCalculationDTO> findAll(Pageable pageable);
    /**
     * Get all the PaymentCalculationDTO where Payment is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PaymentCalculationDTO> findAllWherePaymentIsNull();

    /**
     * Get the "id" paymentCalculation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentCalculationDTO> findOne(Long id);

    /**
     * Delete the "id" paymentCalculation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the paymentCalculation corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentCalculationDTO> search(String query, Pageable pageable);
}
