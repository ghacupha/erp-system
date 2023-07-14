package io.github.erp.erp.depreciation.queue;

/*-
 * Erp System - Mark IV No 2 (Ehud Series) Server ver 1.3.2
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
import io.github.erp.domain.DepreciationBatchSequence;
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.erp.depreciation.DepreciationBatchSequenceService;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.repository.DepreciationBatchSequenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

// import static io.github.erp.erp.depreciation.queue.DepreciationBatchProducer.DEPRECIATION_BATCH_TOPIC;

@Component
public class DepreciationBatchConsumer {

    private static final Logger log = LoggerFactory.getLogger(DepreciationBatchConsumer.class);

    private final DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;
    private final DepreciationBatchSequenceService depreciationBatchSequenceService;

    public DepreciationBatchConsumer(DepreciationBatchSequenceRepository depreciationBatchSequenceRepository, DepreciationBatchSequenceService depreciationBatchSequenceService) {
        this.depreciationBatchSequenceRepository = depreciationBatchSequenceRepository;
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
    }

    @KafkaListener(topics = "depreciation_batch_topic", groupId = "erp-system")
    public void processDepreciationJobMessages(List<DepreciationBatchMessage> messages) {
        // Process the batch of depreciation job messages
        for (DepreciationBatchMessage message : messages) {
            // Extract the necessary details from the message

            log.debug("Received message for batch-id id {}", message.getBatchId());

            depreciationBatchSequenceService.runDepreciation(message);

            log.debug("Depreciation batch-id id {} complete, sequence status update begins...", message.getBatchId());

            DepreciationBatchSequence sequence = depreciationBatchSequenceRepository.getById(Long.valueOf(message.getBatchId()));
            sequence.setDepreciationBatchStatus(DepreciationBatchStatusType.COMPLETED);
            depreciationBatchSequenceRepository.save(sequence);

        }

            // Update the status or progress of the depreciation job, if needed
            // ...
    }
}


