package io.github.erp.erp.depreciation.queue;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import io.github.erp.erp.depreciation.DepreciationBatchSequenceService;
import io.github.erp.erp.depreciation.DepreciationJobCompleteCallback;
import io.github.erp.erp.depreciation.DepreciationJobErroredCallback;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepreciationBatchConsumer {

    private static final Logger log = LoggerFactory.getLogger(DepreciationBatchConsumer.class);

    private final DepreciationBatchSequenceService depreciationBatchSequenceService;
    private final DepreciationJobCompleteCallback depreciationJobCompleteCallback;
    private final DepreciationJobErroredCallback depreciationJobErroredCallback;

    private final Object sequenceLock = new Object(); // For concurrency control

    public DepreciationBatchConsumer(DepreciationBatchSequenceService depreciationBatchSequenceService, DepreciationJobCompleteCallback depreciationJobCompleteCallback, DepreciationJobErroredCallback depreciationJobErroredCallback) {
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
        this.depreciationJobCompleteCallback = depreciationJobCompleteCallback;
        this.depreciationJobErroredCallback = depreciationJobErroredCallback;
    }

    @KafkaListener(topics = "depreciation_batch_topic", groupId = "erp-system", concurrency ="8")
    public void processDepreciationJobMessages(List<DepreciationBatchMessage> messages) {
        // Process the batch of depreciation job messages
        for (DepreciationBatchMessage message : messages) {
            try {
                log.debug("Received message for batch-id id {}", message.getBatchId());

                // TODO review if this method is introducing performance bottlenecks
                // todo due to contention for the same lock
                synchronized (sequenceLock) {
                    // Process and update the batch sequence status
                    // TODO CALLBACK FOR SEQUENCE UPDATE
                    depreciationBatchSequenceService.runDepreciation(message);

                    // TODO implement this in a way that supports parallelism
                    // if (message.isLastBatch()) {
                    //    depreciationJobCompleteCallback.onComplete(message);
                    // }
                }

            } catch (Exception e) {
                depreciationJobErroredCallback.onError(message, e);
            }
        }
    }
}

