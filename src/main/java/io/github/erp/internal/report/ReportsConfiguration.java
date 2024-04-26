package io.github.erp.internal.report;

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
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportsConfiguration {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Bean("prepaymentsReportCache")
    public IMap<String, String> prepaymentsReportCache() {
        return hazelcastInstance.getMap("prepaymentsReportCache");
    }

    @Bean("workInProgressReportCache")
    public IMap<String, String> workInProgressReportCache() {
        return hazelcastInstance.getMap("workInProgressReportCache");
    }
}
