package io.github.erp.service;

import io.github.erp.service.dto.PaymentCategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PaymentCategory}.
 */
public interface PaymentCategoryService {
    /**
     * Save a paymentCategory.
     *
     * @param paymentCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentCategoryDTO save(PaymentCategoryDTO paymentCategoryDTO);

    /**
     * Partially updates a paymentCategory.
     *
     * @param paymentCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentCategoryDTO> partialUpdate(PaymentCategoryDTO paymentCategoryDTO);

    /**
     * Get all the paymentCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentCategoryDTO> findAll(Pageable pageable);

    /**
     * Get all the paymentCategories with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentCategoryDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" paymentCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" paymentCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the paymentCategory corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentCategoryDTO> search(String query, Pageable pageable);
}
