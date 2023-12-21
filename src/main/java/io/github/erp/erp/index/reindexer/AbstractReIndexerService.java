package io.github.erp.erp.index.reindexer;

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
