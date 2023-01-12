package io.github.erp.erp.index;

/*-
 * Erp System - Mark III No 9 (Caleb Series) Server ver 0.4.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Implementation of independent cleanup of the index. The simple implementation involves tearing
 * down the index and building it up again using entries from the DB
 */
public abstract class AbstractReIndexerService implements ApplicationIndexingService, ApplicationReindexingService, ApplicationIndexTearDownService {

    @Override
    @Scheduled(fixedDelayString = "${erp.reIndexer.interval}")
    public void reIndex() {
        this.tearDown();
        this.index();
    }

    @Override
    public abstract void index();

    @Override
    public abstract void tearDown();
}
