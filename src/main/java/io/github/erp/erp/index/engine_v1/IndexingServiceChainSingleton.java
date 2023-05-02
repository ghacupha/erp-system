
/*-
 * Erp System - Mark III No 13 (Caleb Series) Server ver 1.1.3-SNAPSHOT
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
package io.github.erp.erp.index.engine_v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains the list of indexing services registered at startup
 */
public class IndexingServiceChainSingleton {

    private static IndexingServiceChainSingleton instance;

    private IndexingServiceChainSingleton(){}

    private static final Logger log = LoggerFactory.getLogger(IndexingServiceChainSingleton.class);

    private int serviceCount = 1;

    private final List<ControllerIndexingService> indexingServices = new ArrayList<>();

    public static IndexingServiceChainSingleton getInstance() {
        if (instance == null) {
            synchronized (IndexingServiceChainSingleton.class) {
                if (instance == null) {
                    instance = new IndexingServiceChainSingleton();
                }
            }
        }
        return instance;
    }

    public void registerService(ControllerIndexingService service) {

        log.info("Registering service # {}", this.serviceCount++);

        this.indexingServices.add(service);
    }


    List<ControllerIndexingService> getRegisteredIndexingServiceList() {

        log.info("Deploying {} registered services", indexingServices.size());

        return indexingServices;
    }
}
