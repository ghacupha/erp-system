package io.github.erp.erp.depreciation.queue;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.erp.depreciation.BatchSequenceDepreciationService;
import io.github.erp.erp.depreciation.DepreciationJobCompleteCallback;
import io.github.erp.erp.depreciation.DepreciationJobErroredCallback;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.DepreciationBatchSequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepreciationBatchConsumer {

    private static final Logger log = LoggerFactory.getLogger(DepreciationBatchConsumer.class);

    private final BatchSequenceDepreciationService batchSequenceDepreciationService;
    private final DepreciationJobCompleteCallback depreciationJobCompleteCallback;
    private final DepreciationJobErroredCallback depreciationJobErroredCallback;

    private final DepreciationBatchSequenceService depreciationBatchSequenceService;

    private final Object sequenceLock = new Object(); // For concurrency control

    public DepreciationBatchConsumer(
        BatchSequenceDepreciationService batchSequenceDepreciationService,
        DepreciationJobCompleteCallback depreciationJobCompleteCallback,
        DepreciationJobErroredCallback depreciationJobErroredCallback,
        DepreciationBatchSequenceService depreciationBatchSequenceService) {
        this.batchSequenceDepreciationService = batchSequenceDepreciationService;
        this.depreciationJobErroredCallback = depreciationJobErroredCallback;
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
        this.depreciationJobCompleteCallback = depreciationJobCompleteCallback;
    }

    @KafkaListener(topics = "depreciation_batch_topic", groupId = "erp-system")
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
                    batchSequenceDepreciationService.runDepreciation(message);

//                    depreciationBatchSequenceService.findOne(Long.valueOf(message.getBatchId()))
//                        .ifPresent(batch -> {
//                            batch.setDepreciationBatchStatus(DepreciationBatchStatusType.COMPLETED);
//                            depreciationBatchSequenceService.save(batch);
//                        });
                    // depreciationJobCompleteCallback.onComplete(message);

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

