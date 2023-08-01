package io.github.erp.service;

import io.github.erp.service.dto.AgencyNoticeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.AgencyNotice}.
 */
public interface AgencyNoticeService {
    /**
     * Save a agencyNotice.
     *
     * @param agencyNoticeDTO the entity to save.
     * @return the persisted entity.
     */
    AgencyNoticeDTO save(AgencyNoticeDTO agencyNoticeDTO);

    /**
     * Partially updates a agencyNotice.
     *
     * @param agencyNoticeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgencyNoticeDTO> partialUpdate(AgencyNoticeDTO agencyNoticeDTO);

    /**
     * Get all the agencyNotices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgencyNoticeDTO> findAll(Pageable pageable);

    /**
     * Get all the agencyNotices with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgencyNoticeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" agencyNotice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgencyNoticeDTO> findOne(Long id);

    /**
     * Delete the "id" agencyNotice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the agencyNotice corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgencyNoticeDTO> search(String query, Pageable pageable);
}
