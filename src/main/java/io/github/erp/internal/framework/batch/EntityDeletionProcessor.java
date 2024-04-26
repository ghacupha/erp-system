package io.github.erp.internal.framework.batch;

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
