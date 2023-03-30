package io.github.erp.internal.framework.batch;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.5-SNAPSHOT
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
import org.slf4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This abstraction for deletion of entities from the DB in batch
 * @param <Entity>
 */
public class EntityDeletionProcessor<Entity> implements ItemProcessor<List<Long>, List<Entity>> {

    private static final Logger log = getLogger(EntityDeletionProcessor.class);
    private final DeletionService<Entity> deletionService;

    public EntityDeletionProcessor(final DeletionService<Entity> deletionService) {
        this.deletionService = deletionService;
    }

    @Override
    public List<Entity> process(final List<Long> list) throws Exception {
        log.debug("Deletion processor iterating over {} deletion item ids", list.size());
        // TODO THIS FUTURES THING PROBABLY A MISTAKE PLS REMOVE OR ENHANCE
        return deletionService.delete(list).get();
    }
}
