package io.github.erp.service;

import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.LeaseLiabilityScheduleItem}.
 */
public interface LeaseLiabilityScheduleItemService {
    /**
     * Save a leaseLiabilityScheduleItem.
     *
     * @param leaseLiabilityScheduleItemDTO the entity to save.
     * @return the persisted entity.
     */
    LeaseLiabilityScheduleItemDTO save(LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO);

    /**
     * Partially updates a leaseLiabilityScheduleItem.
     *
     * @param leaseLiabilityScheduleItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaseLiabilityScheduleItemDTO> partialUpdate(LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO);

    /**
     * Get all the leaseLiabilityScheduleItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseLiabilityScheduleItemDTO> findAll(Pageable pageable);

    /**
     * Get all the leaseLiabilityScheduleItems with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseLiabilityScheduleItemDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" leaseLiabilityScheduleItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaseLiabilityScheduleItemDTO> findOne(Long id);

    /**
     * Delete the "id" leaseLiabilityScheduleItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the leaseLiabilityScheduleItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseLiabilityScheduleItemDTO> search(String query, Pageable pageable);
}
