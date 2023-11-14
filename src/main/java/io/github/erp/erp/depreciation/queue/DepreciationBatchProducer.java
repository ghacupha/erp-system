package io.github.erp.erp.depreciation.queue;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import io.github.erp.service.dto.DepreciationJobDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creates and sends a depreciation-batch-message to the queue
 */
@Component
public class DepreciationBatchProducer {

    @Value("${kafka.depreciation-batch.topic.name}")
    private String topicName;

    private static final Logger log = LoggerFactory.getLogger(DepreciationBatchProducer.class);

    private final KafkaTemplate<String, DepreciationBatchMessage> kafkaTemplate;

    public DepreciationBatchProducer(
        @Qualifier("depreciationMessageKafkaTemplate") KafkaTemplate<String, DepreciationBatchMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDepreciationJobMessage(DepreciationJobDTO depreciationJob, List<AssetRegistrationDTO> assets, DepreciationBatchSequenceDTO batchSequence, boolean isLastBatch) {
        DepreciationBatchMessage depreciationJobMessage = DepreciationBatchMessage
            .builder()
            .batchId(String.valueOf(batchSequence.getId()))
            .jobId(String.valueOf(depreciationJob.getId()))
            .assetIds(assets.stream().map(AssetRegistrationDTO::getId).map(String::valueOf).collect(Collectors.toList()))
            .initialCost(assets.stream().map(AssetRegistrationDTO::getAssetCost).reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
            .createdAt(LocalDateTime.from(batchSequence.getCreatedAt()))
            .isLastBatch(isLastBatch)
            .startIndex(batchSequence.getStartIndex())
            .endIndex(batchSequence.getEndIndex())
            .createdAt(LocalDateTime.now())
            .build();

        kafkaTemplate.send(topicName, depreciationJobMessage);
    }
}

