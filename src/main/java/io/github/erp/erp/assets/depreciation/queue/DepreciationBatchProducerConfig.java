package io.github.erp.erp.assets.depreciation.queue;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;
import lombok.SneakyThrows;
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
@ConfigurationProperties(prefix = "spring.kafka.topics.depreciation-batch.producer")
public class DepreciationBatchProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, DepreciationBatchMessage> producerFactory() throws ClassNotFoundException {
        DefaultKafkaProducerFactory<String, DepreciationBatchMessage> producerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
        producerFactory.setKeySerializer(new StringSerializer());
        producerFactory.setValueSerializer(new DepreciationBatchMessageSerializer());
        return producerFactory;
    }

    @SneakyThrows
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, String.valueOf(TimeUnit.MINUTES.toMillis(3)));
//        properties.put(ProducerConfig.LINGER_MS_CONFIG, String.valueOf(TimeUnit.MINUTES.toMillis(2)));
//        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, String.valueOf(TimeUnit.MINUTES.toMillis(3)));
//        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, String.valueOf(TimeUnit.MINUTES.toMillis(3)));

        return properties;
    }

    @SneakyThrows
    @Bean("depreciationMessageKafkaTemplate")
    public KafkaTemplate<String, DepreciationBatchMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}



