package io.github.erp.erp.events;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
/**
 * This is an illustration's sample implementation
 */
public class AsynchronousEventConsumer {

    private EventListener listener;

    public AsynchronousEventConsumer(EventListener listener) {
        this.listener = listener;
    }

    public void doAsynchronousOperation(){
        System.out.println("Performing operation in Asynchronous Task");

        new Thread(() -> listener.onTrigger()).start();
    }
}
