package io.github.erp.erp.assets.nbv.queue;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
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
@ConfigurationProperties(prefix = "spring.kafka.topics.nbv.producer")
public class NBVCompilationProducerConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean("nbvProducerFactory")
    public ProducerFactory<String, NBVBatchMessage> nbvProducerFactory() throws ClassNotFoundException {
        DefaultKafkaProducerFactory<String, NBVBatchMessage> producerFactory = new DefaultKafkaProducerFactory<>(nbvProducerConfigs());
        producerFactory.setKeySerializer(new StringSerializer());
        producerFactory.setValueSerializer(new NBVBatchMessageSerializer());
        return producerFactory;
    }

    @SneakyThrows
    @Bean("nbvProducerConfigs")
    public Map<String, Object> nbvProducerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return properties;
    }

    @SneakyThrows
    @Bean("nbvMessageKafkaTemplate")
    public KafkaTemplate<String, NBVBatchMessage> nbvMessageKafkaTemplate() {
        return new KafkaTemplate<>(nbvProducerFactory());
    }
}
