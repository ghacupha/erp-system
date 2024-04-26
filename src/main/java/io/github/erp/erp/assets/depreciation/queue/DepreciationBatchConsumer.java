package io.github.erp.erp.assets.depreciation.queue;

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
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.erp.assets.depreciation.BatchSequenceDepreciationService;
import io.github.erp.erp.assets.depreciation.DepreciationEntrySinkProcessor;
import io.github.erp.erp.assets.depreciation.context.DepreciationAmountContext;
import io.github.erp.erp.assets.depreciation.context.DepreciationJobContext;
import io.github.erp.erp.assets.depreciation.exceptions.UnexpectedDepreciationDataset;
import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.DepreciationBatchSequenceService;
import io.github.erp.service.DepreciationJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DepreciationBatchConsumer {

    private static final Logger log = LoggerFactory.getLogger(DepreciationBatchConsumer.class);

    private final BatchSequenceDepreciationService batchSequenceDepreciationService;
    private final DepreciationJobService depreciationJobService;
    private final DepreciationBatchSequenceService depreciationBatchSequenceService;

    private final DepreciationEntrySinkProcessor depreciationEntrySinkProcessor;

    private final Lock depreciationLock = new ReentrantLock();

    public DepreciationBatchConsumer(BatchSequenceDepreciationService batchSequenceDepreciationService, DepreciationJobService depreciationJobService, DepreciationBatchSequenceService depreciationBatchSequenceService, DepreciationEntrySinkProcessor depreciationEntrySinkProcessor) {
        this.batchSequenceDepreciationService = batchSequenceDepreciationService;
        this.depreciationJobService = depreciationJobService;
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
        this.depreciationEntrySinkProcessor = depreciationEntrySinkProcessor;
    }

    @KafkaListener(topics = "depreciation_batch_topic", groupId = "erp-system-depreciation", concurrency = "8")
    public void processDepreciationJobMessages(DepreciationBatchMessage message, Acknowledgment acknowledgment) {

        try {

            depreciationLock.lock();

            // acknowledge the message to commit offset
            acknowledgment.acknowledge();

            // Process the batch of depreciation job messages
            log.info("Received message for batch-id id {} sequence # {}", message.getBatchId(), message.getSequenceNumber());

            UUID messageCountContextId = message.getContextInstance().getMessageCountContextId();

            DepreciationJobContext contextManager = DepreciationJobContext.getInstance();

            int messagesProcessed = contextManager.getNumberOfProcessedItems(messageCountContextId);

            if (messagesProcessed == message.getNumberOfBatches() | messagesProcessed > message.getNumberOfBatches()) {

                depreciationLock.unlock();

                throw new UnexpectedDepreciationDataset("Number of messages processed = " + messagesProcessed + " Expected number of batches = " + message.getNumberOfBatches());
            }

            long startingTime = System.currentTimeMillis();

            // Depreciation Running...
            boolean messageProcessed = batchSequenceDepreciationService.runDepreciation(message).isProcessed();

            if (messageProcessed) {

                int numberOfProcessed = contextManager.getNumberOfProcessedItems(message.getContextInstance().getDepreciationJobCountUpContextId());

                depreciationBatchSequenceService.findOne(Long.valueOf(message.getBatchId()))
                    .ifPresent(batch -> {
                        batch.setProcessingTime(Duration.ofMillis(System.currentTimeMillis()-startingTime));
                        batch.setProcessedItems(message.getTotalItems());
                        batch.setDepreciationBatchStatus(DepreciationBatchStatusType.COMPLETED);
                        depreciationBatchSequenceService.save(batch);
                    });

                if (message.isLastBatch()) {
                    depreciationEntrySinkProcessor.flushRemainingItems(message.getContextInstance().getDepreciationJobCountDownContextId());

                    // TODO check possible duplication updateDepreciationJobCompleted(message, numberOfProcessed);
                }

                int pendingItemsInTheJob = contextManager.getNumberOfProcessedItems(message.getContextInstance().getDepreciationJobCountDownContextId());


                if (pendingItemsInTheJob == 0 | pendingItemsInTheJob < 0) {

                    DepreciationAmountContext amountContext
                        = DepreciationAmountContext.getDepreciationAmountContext(
                        message.getContextInstance().getDepreciationAmountContextId());

                    int itemsProcessed = amountContext.getNumberOfProcessedItems();

                    depreciationEntrySinkProcessor.flushRemainingItems(message.getContextInstance().getDepreciationJobCountDownContextId());

                    updateDepreciationJobCompleted(message, itemsProcessed);

                    log.info("Depreciation process complete for {} items", itemsProcessed);

                    amountContext.getAmountsByAssetCategoryAndServiceOutlet()
                        .forEach((category, categoryMap) -> categoryMap
                            .forEach((sol, amount) -> log.debug("Depreciation computed for category: {} under service-outlet :{} was {}", category, sol, amount)));

                }

            } // TODO Update depreciation job within this context

        } finally {

            depreciationLock.unlock();
        }
    }

    private void updateDepreciationJobCompleted(DepreciationBatchMessage message, int itemsProcessed) {
        depreciationJobService.findOne(Long.valueOf(message.getJobId()))
            .ifPresent(job -> {
                job.setProcessingTime(Duration.ofNanos(System.nanoTime() - job.getTimeOfCommencement().getNano()));
                job.setProcessedItems(job.getProcessedItems() == null ? itemsProcessed : job.getProcessedItems() + message.getBatchSize());
                job.setDepreciationJobStatus(DepreciationJobStatusType.COMPLETE);
                depreciationJobService.save(job);
            });
    }
}

