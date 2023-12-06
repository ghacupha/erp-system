package io.github.erp.internal.report;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportsConfiguration {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Bean
    public IMap<String, String> prepaymentsReportCache() {
        return hazelcastInstance.getMap("prepaymentsReportCache");
    }
}
