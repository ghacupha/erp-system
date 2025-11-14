package io.github.erp.erp.leases.liability.schedule.batch.support;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.batch.item.file.LineMapper;

/**
 * Decorates an existing {@link LineMapper} by adding a running row number to the
 * mapped item.
 */
public class RowItemLineMapper<T> implements LineMapper<RowItem<T>> {

    private final LineMapper<T> delegate;
    private final AtomicLong rowCounter;

    public RowItemLineMapper(LineMapper<T> delegate) {
        this(delegate, 1);
    }

    public RowItemLineMapper(LineMapper<T> delegate, long startingRow) {
        this.delegate = delegate;
        this.rowCounter = new AtomicLong(startingRow);
    }

    @Override
    public RowItem<T> mapLine(String line, int lineNumber) throws Exception {
        long currentRow = rowCounter.incrementAndGet();
        return new RowItem<>(currentRow, delegate.mapLine(line, lineNumber));
    }
}

