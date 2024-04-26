package io.github.erp.erp.index.reindexer;

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
import io.github.erp.erp.index.api.ApplicationIndexTearDownService;
import io.github.erp.erp.index.api.ApplicationIndexingService;
import io.github.erp.erp.index.api.ApplicationReindexingService;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Implementation of independent cleanup of the index. The simple implementation involves tearing
 * down the index and building it up again using entries from the DB
 */
public abstract class AbstractReIndexerService implements ApplicationIndexingService, ApplicationReindexingService, ApplicationIndexTearDownService {

    @Override
    @Scheduled(fixedDelayString = "${app.ReIndexer.interval}",
        initialDelayString = "${app.ReIndexer.interval}")
    public void reIndex() {
         this.tearDown();
         this.index();
    }

    @Override
    public abstract void index();

    @Override
    public abstract void tearDown();
}
