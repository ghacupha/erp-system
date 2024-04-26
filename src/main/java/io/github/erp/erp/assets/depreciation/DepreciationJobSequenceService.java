package io.github.erp.erp.assets.depreciation;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
