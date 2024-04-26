package io.github.erp.erp.assets.nbv.buffer;

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
import io.github.erp.domain.DepreciationEntry;

import java.util.UUID;

/**
 * The idea is to avoid persisting to the database one item at a time and instead to retain
 * items in a collection which is flushed when we attain a certain level of "fullness", this
 * way we save all times in bigger batches significantly reducing time spent in the persistence
 * layer
 */
public interface BufferedSinkProcessor<T> {

    void addEntry(T entry);

    void flushRemainingItems();

    void shutdown();

    void startup();

    void flushStuckTaskComplete();
}
