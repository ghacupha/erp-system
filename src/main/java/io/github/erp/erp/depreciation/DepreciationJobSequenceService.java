package io.github.erp.erp.depreciation;

import io.github.erp.service.dto.DepreciationJobDTO;

/**
 * The DepreciationJobSequenceService class manages the depreciation process for a given DepreciationJob. It is responsible for
 * triggering the depreciation sequence, processing assets in batches, and updating the depreciation job status accordingly.
 * The class interfaces with various services to fetch assets, create depreciation batch sequences, and enqueue depreciation
 * batch messages for processing.
 *
 * The DepreciationJobSequenceService class provides a way to initiate the depreciation process and handle different scenarios
 * such as closed depreciation periods, completed depreciation jobs, and missing depreciation periods. It takes care of creating
 * depreciation batch sequences and monitoring the enqueuing of batches.
 */
public interface DepreciationJobSequenceService<J> {

    /**
     * Triggers the depreciation process for a given DepreciationJobDTO by processing assets in batches. This method fetches assets
     * from the database, checks for any opt-out conditions, and initiates the depreciation sequence by enqueuing depreciation batch
     * messages for processing.
     *
     * @param J The DTO containing the details of the depreciation job to be processed.
     */
    void  triggerDepreciation(J job);
}
