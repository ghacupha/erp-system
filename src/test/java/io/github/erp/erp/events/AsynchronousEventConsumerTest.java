package io.github.erp.erp.events;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

public class AsynchronousEventConsumerTest {

    @Test
    public void testEventConsumer() {
        EventListener listener = Mockito.mock(AsynchronousEventListenerImpl.class);
        AsynchronousEventConsumer synchronousEventListenerConsumer = new AsynchronousEventConsumer(listener);
        synchronousEventListenerConsumer.doAsynchronousOperation();

        verify(listener, timeout(1000).times(1)).onTrigger();
    }
}
