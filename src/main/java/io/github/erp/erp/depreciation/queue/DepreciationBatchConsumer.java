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
import io.github.erp.erp.depreciation.BatchSequenceDepreciationService;
import io.github.erp.erp.depreciation.context.DepreciationJobContext;
import io.github.erp.erp.depreciation.exceptions.UnexpectedDepreciationDataset;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DepreciationBatchConsumer {

    private static final Logger log = LoggerFactory.getLogger(DepreciationBatchConsumer.class);

    private final BatchSequenceDepreciationService batchSequenceDepreciationService;

    public DepreciationBatchConsumer( BatchSequenceDepreciationService batchSequenceDepreciationService ) {
        this.batchSequenceDepreciationService = batchSequenceDepreciationService;
    }

    @KafkaListener(topics = "depreciation_batch_topic", groupId = "erp-system", concurrency = "8")
    public void processDepreciationJobMessages(DepreciationBatchMessage message, Acknowledgment acknowledgment) {
        // Process the batch of depreciation job messages
        log.debug("Received message for batch-id id {}", message.getBatchId());

        UUID messageCountContextId = message.getDepreciationContextInstance().getMessageCountContextId();

        DepreciationJobContext contextManager = DepreciationJobContext.getInstance();

        int messagesProcessed = contextManager.getNumberOfProcessedItems(messageCountContextId);

        if (messagesProcessed == message.getNumberOfBatches() | messagesProcessed > message.getNumberOfBatches()) {

            throw new UnexpectedDepreciationDataset("Number of messages processed = " + messagesProcessed + " Expected number of batches = " + message.getNumberOfBatches());
        }

        // acknowledge the message to commit offset
        acknowledgment.acknowledge();

        // Depreciation Running...
        boolean messageProcessed = batchSequenceDepreciationService.runDepreciation(message).isProcessed();

        if (messageProcessed) {

            contextManager.updateNumberOfProcessedItems(messageCountContextId, 1);

            contextManager.updateNumberOfProcessedItems(
                message.getDepreciationContextInstance().getDepreciationBatchCountDownContextId(), message.getBatchSize());
        }
    }
}

