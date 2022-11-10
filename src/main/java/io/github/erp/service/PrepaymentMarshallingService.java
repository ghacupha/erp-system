package io.github.erp.service;

import io.github.erp.service.dto.PrepaymentMarshallingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PrepaymentMarshalling}.
 */
public interface PrepaymentMarshallingService {
    /**
     * Save a prepaymentMarshalling.
     *
     * @param prepaymentMarshallingDTO the entity to save.
     * @return the persisted entity.
     */
    PrepaymentMarshallingDTO save(PrepaymentMarshallingDTO prepaymentMarshallingDTO);

    /**
     * Partially updates a prepaymentMarshalling.
     *
     * @param prepaymentMarshallingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrepaymentMarshallingDTO> partialUpdate(PrepaymentMarshallingDTO prepaymentMarshallingDTO);

    /**
     * Get all the prepaymentMarshallings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentMarshallingDTO> findAll(Pageable pageable);

    /**
     * Get all the prepaymentMarshallings with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentMarshallingDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" prepaymentMarshalling.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrepaymentMarshallingDTO> findOne(Long id);

    /**
     * Delete the "id" prepaymentMarshalling.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the prepaymentMarshalling corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentMarshallingDTO> search(String query, Pageable pageable);
}
