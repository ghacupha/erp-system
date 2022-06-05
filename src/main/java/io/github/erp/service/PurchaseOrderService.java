package io.github.erp.service;

import io.github.erp.service.dto.PurchaseOrderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PurchaseOrder}.
 */
public interface PurchaseOrderService {
    /**
     * Save a purchaseOrder.
     *
     * @param purchaseOrderDTO the entity to save.
     * @return the persisted entity.
     */
    PurchaseOrderDTO save(PurchaseOrderDTO purchaseOrderDTO);

    /**
     * Partially updates a purchaseOrder.
     *
     * @param purchaseOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PurchaseOrderDTO> partialUpdate(PurchaseOrderDTO purchaseOrderDTO);

    /**
     * Get all the purchaseOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchaseOrderDTO> findAll(Pageable pageable);

    /**
     * Get all the purchaseOrders with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchaseOrderDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" purchaseOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PurchaseOrderDTO> findOne(Long id);

    /**
     * Delete the "id" purchaseOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the purchaseOrder corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchaseOrderDTO> search(String query, Pageable pageable);
}
