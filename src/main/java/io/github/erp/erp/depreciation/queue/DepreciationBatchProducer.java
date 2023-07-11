package io.github.erp.erp.depreciation.queue;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.1
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationJobDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creates and sends a depreciation-batch-message to the queue
 */
@Component
public class DepreciationBatchProducer {

    public static final String DEPRECIATION_BATCH_TOPIC = "depreciation_batch_topic";

    private static final Logger log = LoggerFactory.getLogger(DepreciationBatchProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public DepreciationBatchProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendDepreciationJobMessage(DepreciationJobDTO depreciationJob, List<AssetRegistrationDTO> assets, BigDecimal initialCost, BigDecimal residualValue, int usefulLifeYears) {
        try {
            DepreciationBatchMessage depreciationJobMessage = new DepreciationBatchMessage();
            depreciationJobMessage.setJobId(String.valueOf(depreciationJob.getId()));
            depreciationJobMessage.setAssetIds(assets.stream().map(AssetRegistrationDTO::getId).map(String::valueOf).collect(Collectors.toList()));
            depreciationJobMessage.setInitialCost(assets.stream().map(AssetRegistrationDTO::getAssetCost).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            depreciationJobMessage.setResidualValue(assets.stream().map(AssetRegistrationDTO::getAssetCost).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));

            String messageValue = objectMapper.writeValueAsString(depreciationJobMessage);
            kafkaTemplate.send(DEPRECIATION_BATCH_TOPIC, messageValue);
        } catch (JsonProcessingException e) {
            // Handle serialization exception
        }
    }
}

