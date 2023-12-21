package io.github.erp.erp.depreciation.queue;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import com.fasterxml.jackson.databind.JsonSerializer;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.kafka.producer")
public class DepreciationBatchProducerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Set Kafka producer properties
    @Bean
    public ProducerFactory<String, DepreciationBatchMessage> producerFactory() throws ClassNotFoundException {
        DefaultKafkaProducerFactory<String, DepreciationBatchMessage> producerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
        producerFactory.setKeySerializer(new StringSerializer());
        producerFactory.setValueSerializer(new DepreciationBatchMessageSerializer());
        return producerFactory;
    }

    // Define Kafka producer configurations
    @SneakyThrows
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Add additional properties as needed
        return properties;
    }

    // Create KafkaTemplate for producing messages
    @SneakyThrows
    @Bean("depreciationMessageKafkaTemplate")
    public KafkaTemplate<String, DepreciationBatchMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}



