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
import io.github.erp.internal.framework.BatchService;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * Performs persistence of the newly created entities through the batch-service
 *
 * @param <DTO>
 */
public class EntityListItemsWriter<DTO> implements ItemWriter<List<DTO>> {

    private final BatchService<DTO> batchService;

    public EntityListItemsWriter(BatchService<DTO> batchService) {
        this.batchService = batchService;
    }

    @Override
    public void write(List<? extends List<DTO>> items) throws Exception {
        items.stream().peek(batchService::save).forEach(batchService::index);
    }
}
