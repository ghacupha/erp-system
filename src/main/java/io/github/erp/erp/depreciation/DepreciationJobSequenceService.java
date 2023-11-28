package io.github.erp.erp.depreciation;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
public interface DepreciationJobSequenceService<Job> {

    /**
     * Triggers the depreciation process for a given DepreciationJobDTO by processing assets in batches. This method fetches assets
     * from the database, checks for any opt-out conditions, and initiates the depreciation sequence by enqueuing depreciation batch
     * messages for processing.
     *
     * @param Job The DTO containing the details of the depreciation job to be processed.
     */
    void  triggerDepreciation(Job job);
}
