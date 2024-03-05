package io.github.erp.config;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring")
public class KafkaProperties {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    private Map<String, String> consumer = new HashMap<>();

    private Map<String, String> producer = new HashMap<>();

    public String getBootStrapServers() {
        return bootStrapServers;
    }

    public void setBootStrapServers(String bootStrapServers) {
        this.bootStrapServers = bootStrapServers;
    }

    public Map<String, Object> getConsumerProps() {
        Map<String, Object> properties = new HashMap<>(this.consumer);
        if (!properties.containsKey("bootstrap.servers")) {
            properties.put("bootstrap.servers", this.bootStrapServers);
        }
        return properties;
    }

    public void setConsumer(Map<String, String> consumer) {
        this.consumer = consumer;
    }

    public Map<String, Object> getProducerProps() {
        Map<String, Object> properties = new HashMap<>(this.producer);
        if (!properties.containsKey("bootstrap.servers")) {
            properties.put("bootstrap.servers", this.bootStrapServers);
        }
        return properties;
    }

    public void setProducer(Map<String, String> producer) {
        this.producer = producer;
    }
}
