package io.github.erp.erp.assets.nbv.queue;

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
