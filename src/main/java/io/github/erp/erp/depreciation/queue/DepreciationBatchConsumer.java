package io.github.erp.erp.depreciation.queue;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.5
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
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.erp.depreciation.DepreciationBatchSequenceService;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.repository.DepreciationBatchSequenceRepository;
import io.github.erp.repository.DepreciationJobRepository;
import io.github.erp.service.DepreciationJobNoticeService;
import io.github.erp.service.dto.DepreciationJobNoticeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DepreciationBatchConsumer {

    private static final Logger log = LoggerFactory.getLogger(DepreciationBatchConsumer.class);

    private final DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;
    private final DepreciationBatchSequenceService depreciationBatchSequenceService;
    private final DepreciationJobRepository depreciationJobRepository;
    private final DepreciationJobNoticeService depreciationJobNoticeService;

    private final Object sequenceLock = new Object(); // For concurrency control
    private final AtomicInteger completedBatchCount = new AtomicInteger(0);

    public DepreciationBatchConsumer(DepreciationBatchSequenceRepository depreciationBatchSequenceRepository, DepreciationBatchSequenceService depreciationBatchSequenceService, DepreciationJobRepository depreciationJobRepository, DepreciationJobNoticeService depreciationJobNoticeService) {
        this.depreciationBatchSequenceRepository = depreciationBatchSequenceRepository;
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
        this.depreciationJobRepository = depreciationJobRepository;
        this.depreciationJobNoticeService = depreciationJobNoticeService;
    }

    @KafkaListener(topics = "depreciation_batch_topic", groupId = "erp-system", concurrency ="8")
    public void processDepreciationJobMessages(List<DepreciationBatchMessage> messages) {
        // Process the batch of depreciation job messages
        for (DepreciationBatchMessage message : messages) {
            try {
                log.debug("Received message for batch-id id {}", message.getBatchId());

                synchronized (sequenceLock) {
                    // Process and update the batch sequence status
                    // TODO CALLBACK FOR SEQUENCE UPDATE
                    depreciationBatchSequenceService.runDepreciation(message);
                    // updateBatchSequenceStatus(message.getBatchId(), DepreciationBatchStatusType.COMPLETED);

                    completedBatchCount.incrementAndGet(); // Increment the completed batch count

                    // Check if all batch sequences have completed
                    if (completedBatchCount.get() == messages.size()) {
                        // Check if all batch sequences have completed
                        if (allBatchSequencesCompleted(messages)) {
                            // todo check if last batch, and check if in sync
                            if (message.isLastBatch()) {
                                // todo now listen if you do this at the wrong time the whole process is done
                                updateDepreciationJobStatus(message.getJobId(), DepreciationJobStatusType.COMPLETE);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                log.error("Error processing batch-id id {}", message.getBatchId(), e);
                // Handle errors here, such as sending notifications or retries

                DepreciationJobNoticeDTO jobNotice = new DepreciationJobNoticeDTO();
                jobNotice.setEventTimeStamp(ZonedDateTime.now());
                jobNotice.setEventNarrative("Error encountered processing batch-id id ,"+ message.getBatchId());
                jobNotice.setErrorMessage(e.getMessage());
                jobNotice.setSourceModule("DepreciationBatchConsumer");

                depreciationJobNoticeService.save(jobNotice);

                updateDepreciationJobStatus(message.getJobId(), DepreciationJobStatusType.ERRORED);
                updateBatchSequenceStatus(message.getBatchId(), DepreciationBatchStatusType.ERRORED);
            }

            // Check if all batch sequences have completed
            if (completedBatchCount.get() == messages.size()) {
                // Check if all batch sequences have completed
                if (allBatchSequencesCompleted(messages)) {
                    // todo check if last batch, and check if in sync
                    // todo now listen if you do this at the wrong time the whole process is done
                    // todo check if last batch, and check if in sync
                    if (message.isLastBatch()) {
                        updateDepreciationJobStatus(messages.get(0).getJobId(), DepreciationJobStatusType.COMPLETE);
                    }
                }
            }
        }



    }

    private boolean allBatchSequencesCompleted(List<DepreciationBatchMessage> messages) {
        return messages.stream()
            .allMatch(message -> {
                DepreciationBatchSequence sequence = depreciationBatchSequenceRepository.findById(Long.valueOf(message.getBatchId())).orElse(null);
                return sequence != null && sequence.getDepreciationBatchStatus() == DepreciationBatchStatusType.COMPLETED;
            });
    }

    private void updateDepreciationJobStatus(String jobId, DepreciationJobStatusType status) {
        Long id = Long.valueOf(jobId);
        depreciationJobRepository.findById(id).ifPresent(depreciationJob -> {
            if (depreciationJob.getDepreciationJobStatus() != status) {
                depreciationJob.setDepreciationJobStatus(status);
                depreciationJobRepository.save(depreciationJob);

                log.info("The depreciation job id: {} {}, has been completed", depreciationJob.getId(), depreciationJob.getDescription());
            }
        });
    }

    private void updateBatchSequenceStatus(String batchId, DepreciationBatchStatusType status) {
        depreciationBatchSequenceRepository.findById(Long.valueOf(batchId)).ifPresent(sequence -> {
            sequence.setDepreciationBatchStatus(status);
            depreciationBatchSequenceRepository.save(sequence);
        });
    }
}

