package io.github.erp.erp.assets.nbv.buffer;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
