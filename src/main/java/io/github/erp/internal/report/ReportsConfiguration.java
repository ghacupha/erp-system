package io.github.erp.internal.report;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
