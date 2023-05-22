package io.github.erp.service;

import io.github.erp.service.dto.SettlementRequisitionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SettlementRequisition}.
 */
public interface SettlementRequisitionService {
    /**
     * Save a settlementRequisition.
     *
     * @param settlementRequisitionDTO the entity to save.
     * @return the persisted entity.
     */
    SettlementRequisitionDTO save(SettlementRequisitionDTO settlementRequisitionDTO);

    /**
     * Partially updates a settlementRequisition.
     *
     * @param settlementRequisitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SettlementRequisitionDTO> partialUpdate(SettlementRequisitionDTO settlementRequisitionDTO);

    /**
     * Get all the settlementRequisitions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementRequisitionDTO> findAll(Pageable pageable);

    /**
     * Get all the settlementRequisitions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementRequisitionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" settlementRequisition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SettlementRequisitionDTO> findOne(Long id);

    /**
     * Delete the "id" settlementRequisition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the settlementRequisition corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementRequisitionDTO> search(String query, Pageable pageable);
}
